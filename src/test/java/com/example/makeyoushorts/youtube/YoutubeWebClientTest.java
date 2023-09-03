package com.example.makeyoushorts.youtube;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class YoutubeWebClientTest {
    private final YoutubeWebClient youtubeWebClient;

    @Autowired
    public YoutubeWebClientTest(YoutubeWebClient youtubeWebClient) {
        this.youtubeWebClient = youtubeWebClient;
    }

    @Test
    public void returnsHeatMapHTML() {
        // given
        String videoId = "Gr-BGf7rzrY";

        // when
        String heatMapHtml = youtubeWebClient.fetchYtpHeatMapHtmlByVideoId(videoId);

        // then
        System.out.println(heatMapHtml);

        // heatmap SVG tag always starts with `M 0.0,100,0` & ends with `1000.0,100.0`
        Assertions.assertThat(heatMapHtml.startsWith("M 0.0,100.0")).isTrue();
        Assertions.assertThat(heatMapHtml.endsWith("1000.0,100.0")).isTrue();
    }

    @Test
    public void returnsNullWhenHtmlNotFound() {
        // heatmap svg element not found on insufficient view video
        // given
        String videoId = "8pU8DQ1unGo";

        // when
        String heatMapHtml = youtubeWebClient.fetchYtpHeatMapHtmlByVideoId(videoId);

        // then
        Assertions.assertThat(heatMapHtml).isNull();
    }
}