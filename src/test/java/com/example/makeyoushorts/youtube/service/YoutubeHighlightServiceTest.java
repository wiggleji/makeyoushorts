package com.example.makeyoushorts.youtube.service;

import com.example.makeyoushorts.youtube.dto.YoutubeTopFiveHighlightsDto;
import com.example.makeyoushorts.youtube.service.YoutubeHeatmapService;
import com.example.makeyoushorts.youtube.service.YoutubeHighlightService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class YoutubeHighlightServiceTest {

    private final YoutubeHighlightService youtubeHighlightService;

    private final YoutubeHeatmapService youtubeHeatmapService;

    @Autowired
    public YoutubeHighlightServiceTest(YoutubeHighlightService youtubeHighlightService, YoutubeHeatmapService youtubeHeatmapService) {
        this.youtubeHighlightService = youtubeHighlightService;
        this.youtubeHeatmapService = youtubeHeatmapService;
    }

    @Test
    public void retrieveTopFiveHighlightYoutubeVideo__returns_top_five_highlights() {
        // given
        String heatmapVideoId = "Gr-BGf7rzrY";

        // when
        YoutubeTopFiveHighlightsDto result = youtubeHighlightService.retrieveTopFiveHighlightYoutubeVideo(heatmapVideoId);

        // then
        Assertions.assertThat(result.topHighlights.size()).isEqualTo(5);
    }
}