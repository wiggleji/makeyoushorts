package com.example.makeyoushorts.youtube.controller;

import com.example.makeyoushorts.youtube.dto.YoutubeTopFiveHighlightsDto;
import com.example.makeyoushorts.youtube.service.YoutubeHighlightService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

@WebMvcTest(YoutubeHighlightController.class)
public class YoutubeHighlightControllerTest {

    @MockBean
    private YoutubeHighlightService highlightService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getTopFiveHighlights__returns_ResponseEntity_YoutubeTopFiveHighlightsDto() throws Exception {
        // given
        String testVideoId = "Gr-BGf7rzrY?si=L5YYMTiLvNDWYpp9";
        ArrayList<ArrayList<Integer>> mockSeconds = new ArrayList<>();
        mockSeconds.add(new ArrayList<>(Arrays.asList(2115, 2180)));
        mockSeconds.add(new ArrayList<>(Arrays.asList(0, 35)));
        mockSeconds.add(new ArrayList<>(Arrays.asList(2408, 2473)));
        mockSeconds.add(new ArrayList<>(Arrays.asList(2440, 2506)));
        mockSeconds.add(new ArrayList<>(Arrays.asList(3190, 3255)));
        YoutubeTopFiveHighlightsDto mockHighlightsDto = YoutubeTopFiveHighlightsDto.builder()
                .topHighlights(mockSeconds)
                .build();

        when(highlightService.retrieveTopFiveHighlightYoutubeVideo(testVideoId)).thenReturn(mockHighlightsDto);

        // when
        ResultActions youtuBeResultAction = mockMvc.perform(get("/highlights")
                .param("videoUrl", String.format("https://youtu.be/%s", testVideoId))
                .characterEncoding("utf-8"));
        ResultActions youtubeResultAction = mockMvc.perform(get("/highlights")
                .param("videoUrl", String.format("https://www.youtube.com/watch?v=%s", testVideoId))
                .characterEncoding("utf-8"));

        // then
        youtuBeResultAction
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                        content().encoding("UTF-8"),
                        jsonPath("$.topHighlights").isNotEmpty()
                );
        youtubeResultAction
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                        content().encoding("UTF-8"),
                        jsonPath("$.topHighlights").isNotEmpty()
                );
    }

    @Test
    public void getTopFiveHighlights__throws_YoutubeUrlException() throws Exception {
        // given

        // when
        ResultActions resultAction = mockMvc.perform(get("/highlights")
                .param("videoUrl", "InvalidURL")
                .characterEncoding("utf-8"));

        // then
        resultAction
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
}