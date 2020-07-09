package com.xxAMIDOxx.xxSTACKSxx;

import com.microsoft.azure.spring.autoconfigure.cosmosdb.CosmosAutoConfiguration;
import com.microsoft.azure.spring.autoconfigure.cosmosdb.CosmosDbRepositoriesAutoConfiguration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(exclude = {
        CosmosDbRepositoriesAutoConfiguration.class,
        CosmosAutoConfiguration.class
})
public class Application {

    private static final String PROJECT_URL = "https://github.com/amido/stacks-java";

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    /**
     * OAS/Swagger Configuration
     */
    @Bean
    public OpenAPI customOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Menu API")
                        .version("1.0")
                        .description("APIs used to interact and manage menus for a restaurant")
                        .contact(new Contact()
                                .name("Amido")
                                .url(PROJECT_URL)
                                .email("stacks@amido.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://springdoc.org")));

    }
}

