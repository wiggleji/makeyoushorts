package com.example.makeyoushorts.youtube;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class YoutubeWebClient {
    private final WebClient webClient;

    public YoutubeWebClient(WebClient webClient) {
        this.webClient = webClient.mutate()
                .baseUrl("https://www.youtube.com/")
                .build();
    }

    public void testRequest() {
        String block = webClient.mutate()
                .build()
                .get()
                .uri("/watch?v=Gr-BGf7rzrY")
                .retrieve()
                .bodyToMono(String.class).block();
        System.out.println(block);
    }
}
