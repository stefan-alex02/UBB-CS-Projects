package ro.mpp2024.restclient.client;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RestClient;
import ro.mpp2024.domain.Configuration;
import ro.mpp2024.domain.Game;
import ro.mpp2024.restclient.exceptions.ServiceException;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;

public class GameClient {
    public static final String GAMES_URL = "http://localhost:8080/hintgame/games";
    public static final String CONFIGURATIONS_URL = "http://localhost:8080/hintgame/configurations";
    RestClient restClient = RestClient.builder()
            .requestInterceptor(new CustomRestClientInterceptor())
            .build();

    private <T> T execute(Callable<T> callable) {
        try {
            return callable.call();
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    public Collection<Game> getGamesOfPlayer(String alias) {
        return List.of(execute(() ->
                restClient.get()
                        .uri(String.format("%s?alias=%s", GAMES_URL, alias))
                        .retrieve()
                        .body(Game[].class)));
    }

    public String addConfiguration(Configuration configuration) {
        return execute(() -> restClient.post().uri(CONFIGURATIONS_URL).body(configuration).retrieve().body(String.class));
    }

    public static class CustomRestClientInterceptor implements ClientHttpRequestInterceptor {
        @Override
        public ClientHttpResponse intercept(
                HttpRequest request,
                byte[] body,
                ClientHttpRequestExecution execution) throws IOException {
            System.out.println("Sending a "+request.getMethod()+ " request to "+request.getURI()+ " and body ["+new String(body)+"]");
            ClientHttpResponse response=null;
            try {
                response = execution.execute(request, body);
                System.out.println("Got response code " + response.getStatusCode());
            }catch(IOException ex){
                System.err.println("Eroare executie "+ex);
            }
            return response;
        }
    }
}
