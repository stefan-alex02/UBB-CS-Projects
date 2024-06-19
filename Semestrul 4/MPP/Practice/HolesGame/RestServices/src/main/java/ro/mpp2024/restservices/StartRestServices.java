package ro.mpp2024.restservices;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import java.io.IOException;
import java.util.Properties;

@SpringBootApplication
public class StartRestServices {
    public static void main(String[] args) {
        SpringApplication.run(StartRestServices.class, args);
    }

    @Bean(name="props")
    @Primary
    public Properties getBdProperties(){
        Properties props = new Properties();
        try {
            System.out.println("Searching bd.config in directory ");
            props.load(getClass().getClassLoader().getResourceAsStream("bd.config"));
        } catch (IOException e) {
            System.err.println("Configuration file bd.cong not found" + e);
        }
        return props;
    }
}