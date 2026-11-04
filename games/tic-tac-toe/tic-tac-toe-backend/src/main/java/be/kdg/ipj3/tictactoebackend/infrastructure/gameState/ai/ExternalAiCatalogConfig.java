package be.kdg.ipj3.tictactoebackend.infrastructure.gameState.ai;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class ExternalAiCatalogConfig {
    @Bean("aiCatalogApi")
    RestClient ExternalAiCatalogTemplate(@Value("${ai-catalog-api.url}") final String url) {
        return RestClient.create(url);
    }
}
