package be.kdg.ipj3.platformbackend.chatbot.infrastructure.ai;

import be.kdg.ipj3.platformbackend.chatbot.api.dtos.chatbot.ChatbotAnswerDto;
import be.kdg.ipj3.platformbackend.chatbot.api.dtos.chatbot.ChatbotRequestDto;
import be.kdg.ipj3.platformbackend.chatbot.domain.catalog.AiChatbotApiCatalog;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClient;

@Slf4j
@Component
public class ExternalAIChatbotApiCatalog implements AiChatbotApiCatalog {
    private final RestClient restClient;
    private final String chatbotUrl;

    public ExternalAIChatbotApiCatalog(RestClient restClient, @Value("${ai-chatbot-api.url}") String chatbotUrl) {
        this.restClient = restClient;
        this.chatbotUrl = chatbotUrl;
    }

    @Override
    public ChatbotAnswerDto askQuestion(ChatbotRequestDto body) {
        log.info("Requesting response from chatbot");
        try {
            JsonNode responseBody = restClient.post()
                    .uri(chatbotUrl)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                    .body(body)
                    .retrieve()
                    .body(JsonNode.class);

            if (responseBody == null) {
                throw new IllegalStateException("Chatbot returned empty response body");
            }

            String answer = responseBody.has("answer")
                    ? responseBody.get("answer").asText()
                    : null;

            String error = responseBody.has("error")
                    ? responseBody.get("error").asText()
                    : null;

            if (answer == null && error == null) {
                throw new IllegalStateException(
                        "Chatbot response missing both 'answer' and 'error' fields"
                );
            }

            if (answer.equals("null") || answer.equals("No games have been uploaded for your team yet.")) {
                answer = "Can you please rephrase your question?";
            }

            return new ChatbotAnswerDto(answer, error);

        } catch (HttpStatusCodeException e) {
            log.warn("Http error while asking chatbot: {}", e.getResponseBodyAsString());
            return new ChatbotAnswerDto(null, "Chatbot service unavailable");
        }
    }
}
