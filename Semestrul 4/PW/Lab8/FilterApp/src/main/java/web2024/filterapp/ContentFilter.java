package web2024.filterapp;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebFilter("/*")
public class ContentFilter implements Filter {

    private List<Pattern> patterns = new ArrayList<>();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        loadPatternsFromDatabase();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        HttpServletResponseWrapper wrappedResponse = new HttpServletResponseWrapper((HttpServletResponse) response) {
            @Override
            public PrintWriter getWriter() throws IOException {
                return pw;
            }
        };

        chain.doFilter(request, wrappedResponse);

        loadPatternsFromDatabase();

        pw.flush();
        String originalContent = sw.toString();
        String filteredContent = filterContent(originalContent);

        response.getWriter().write(filteredContent);
    }

    @Override
    public void destroy() {
    }

    private void loadPatternsFromDatabase() {
        try (Connection connection = DBUtils.getConnection()) {
            patterns.clear();
            PreparedStatement ps = connection.prepareStatement("SELECT pattern FROM regex_patterns");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                patterns.add(Pattern.compile(rs.getString("pattern")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String filterContent(String content) {
        StringBuilder filteredContent = new StringBuilder();
        int lastEndIndex = 0;

        for (Pattern pattern : patterns) {
            Matcher matcher = pattern.matcher(content);

            while (matcher.find()) {
                filteredContent.append(content.substring(lastEndIndex, matcher.start()));

                String replacement = String.valueOf(matcher.group()).replaceAll(".", "*");
                filteredContent.append(replacement);

                lastEndIndex = matcher.end();
            }
        }
        filteredContent.append(content.substring(lastEndIndex));

        return filteredContent.toString();
    }

}
