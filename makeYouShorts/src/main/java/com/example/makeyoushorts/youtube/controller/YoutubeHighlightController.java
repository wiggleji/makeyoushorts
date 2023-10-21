package com.example.makeyoushorts.youtube.controller;

import com.example.makeyoushorts.youtube.dto.YoutubeTopFiveHighlightsDto;
import com.example.makeyoushorts.youtube.exception.YoutubeUrlException;
import com.example.makeyoushorts.youtube.service.YoutubeHighlightService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class YoutubeHighlightController {

    private final YoutubeHighlightService highlightService;

    public YoutubeHighlightController(YoutubeHighlightService highlightService) {
        this.highlightService = highlightService;
    }

    @GetMapping(value = "/highlights", produces = "application/json; charset=UTF-8")
    public ResponseEntity<YoutubeTopFiveHighlightsDto> getTopFiveHighlights(@RequestParam String videoUrl) {
        String videoId = retrieveVideoId(videoUrl);
        if (videoId == null) throw new YoutubeUrlException("videoUrl is not valid YouTube URL");

        YoutubeTopFiveHighlightsDto highlightsDto = highlightService.retrieveTopFiveHighlightYoutubeVideo(videoId);

        return ResponseEntity.ok(highlightsDto);
    }

    private String retrieveVideoId(String videoUrl) {
        if (videoUrl.contains("youtu.be")) {
            // https://youtu.be/Gr-BGf7rzrY?si=L5YYMTiLvNDWYpp9
            String[] urlStrings = videoUrl.split("/");
            // prevent start seconds parameter
            return urlStrings[urlStrings.length - 1].split("&")[0];
        } else if (videoUrl.contains("youtube.com")) {
            // https://www.youtube.com/watch?v=Gr-BGf7rzrY&t=2202
            String[] urlStrings = videoUrl.split("v=");
            // prevent start seconds parameter
            return urlStrings[urlStrings.length - 1].split("&")[0];
        } else return null;
    }
}
