package com.example.makeyoushorts.youtube;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class YoutubeWebClientTest {
    private YoutubeWebClient youtubeWebClient;

    @Autowired
    public YoutubeWebClientTest(YoutubeWebClient youtubeWebClient) {
        this.youtubeWebClient = youtubeWebClient;
    }

    @Test
    public void testGEt() {
        // given
        youtubeWebClient.testRequest();

        // when

        // then

    }
}