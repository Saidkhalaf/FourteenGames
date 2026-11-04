package be.kdg.gobackend.registration.infrastructure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@Slf4j
@Component
public class UrlChecker {
    public boolean isUrlReachable(String url) {
        log.info("Checking if the url: {} is reachable", url);

        url = url.replace("localhost", "[::1]");

        HttpClient client = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).build();
        HttpRequest request = HttpRequest.newBuilder(URI.create(url))
                .timeout(Duration.ofSeconds(5))
                .method("HEAD", HttpRequest.BodyPublishers.noBody())
                .build();

        try {
            return client.send(request, HttpResponse.BodyHandlers.discarding()).statusCode() == 200;
        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Url: {} is unreachable {}", url, e.getMessage());
            return false;
        }
    }
}
