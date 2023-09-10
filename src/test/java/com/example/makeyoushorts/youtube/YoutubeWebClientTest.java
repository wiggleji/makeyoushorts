package com.example.makeyoushorts.youtube;

import com.example.makeyoushorts.youtube.dto.YoutubeVideoInfoDto;
import com.example.makeyoushorts.youtube.util.YoutubeWebClient;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
        YoutubeVideoInfoDto videoInfoDto = youtubeWebClient.fetchYtpHeatMapHtmlByVideoId(videoId);

        // then

        // heatmap SVG tag always starts with `M 0.0,100,0` & ends with `1000.0,100.0`
        Assertions.assertThat(videoInfoDto.getVideoLength() > 0).isTrue();
        Assertions.assertThat(videoInfoDto.getYtpHeatMapPathD().startsWith("M 0.0,100.0")).isTrue();
        Assertions.assertThat(videoInfoDto.getYtpHeatMapPathD().endsWith("1000.0,100.0")).isTrue();
    }

    @Test
    public void returnsNullWhenHtmlNotFound() {
        // heatmap svg element not found on insufficient view video
        // given
        String videoId = "8pU8DQ1unGo";

        // when
        YoutubeVideoInfoDto videoInfoDto = youtubeWebClient.fetchYtpHeatMapHtmlByVideoId(videoId);

        // then
        Assertions.assertThat(videoInfoDto).isNull();
    }
}