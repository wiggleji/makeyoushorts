package com.example.makeyoushorts.youtube;

import com.example.makeyoushorts.youtube.dto.YoutubeBezierCurveDto;
import com.example.makeyoushorts.youtube.exception.HeatMapNotFoundException;
import com.example.makeyoushorts.youtube.service.YoutubeHeatmapService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;

@SpringBootTest
class YoutubeHeatmapServiceTest {

    private final YoutubeHeatmapService youtubeHeatmapService;

    @Autowired
    public YoutubeHeatmapServiceTest(YoutubeHeatmapService youtubeHeatmapService) {
        this.youtubeHeatmapService = youtubeHeatmapService;
    }

    @Test
    public void getYouTubeVideoBezierCurvePoints__returns_YoutubeBezierCurveDto() {
        // given
        String heatmapVideoId = "Gr-BGf7rzrY";

        // when
        YoutubeBezierCurveDto bezierCurveDto = youtubeHeatmapService.getYoutubeBezierCurveDtoFromYoutubeVideo(heatmapVideoId);

        // then
        Assertions.assertThat(bezierCurveDto.getVideoLength() > 0).isTrue();
        Assertions.assertThat(bezierCurveDto.startPoint).isEqualTo(new ArrayList<Float>(Arrays.asList(0.0F, 100.0F)));
        Assertions.assertThat(bezierCurveDto.bezierCurvePoints.size()).isNotEqualTo(0);
    }

    @Test
    public void getYouTubeVideoBezierCurvePoints__throws_HeatMapNotFoundException() {
        // given
        String noHeatmapVideoId = "8pU8DQ1unGo";

        // when

        // then
        org.junit.jupiter.api.Assertions.assertThrows(HeatMapNotFoundException.class, () -> {
            youtubeHeatmapService.getYoutubeBezierCurveDtoFromYoutubeVideo(noHeatmapVideoId);
        });
    }
}