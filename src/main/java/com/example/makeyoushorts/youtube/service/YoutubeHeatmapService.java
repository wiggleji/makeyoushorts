package com.example.makeyoushorts.youtube.service;

import com.example.makeyoushorts.youtube.dto.YoutubeBezierCurveDto;
import com.example.makeyoushorts.youtube.dto.YoutubeVideoInfoDto;
import com.example.makeyoushorts.youtube.exception.HeatMapNotFoundException;
import com.example.makeyoushorts.youtube.util.CubicBezierCurveExtractor;
import com.example.makeyoushorts.youtube.util.YoutubeWebClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Slf4j
@Service
public class YoutubeHeatmapService {

    private final YoutubeWebClient youtubeWebClient;
    private final CubicBezierCurveExtractor bezierCurveExtractor;

    public YoutubeHeatmapService(YoutubeWebClient youtubeWebClient, CubicBezierCurveExtractor cubicBezierCurveExtractor) {
        this.youtubeWebClient = youtubeWebClient;
        this.bezierCurveExtractor = cubicBezierCurveExtractor;
    }

    public YoutubeBezierCurveDto getYoutubeBezierCurveDtoFromYoutubeVideo(String videoId) {
        YoutubeVideoInfoDto videoInfoDto = youtubeWebClient.fetchYtpHeatMapHtmlByVideoId(videoId);

        if (videoInfoDto == null)
            // throws HeatMapNotFoundException if no heatmap is found
            throw new HeatMapNotFoundException(String.format("Failed to get heatmap from Youtube| ID: %s", videoId));

        ArrayList<Float> startPoint = bezierCurveExtractor
                .getBezierStartPoint(videoInfoDto.getYtpHeatMapPathD());
        ArrayList<ArrayList<ArrayList<Float>>> bezierCurvePoints = bezierCurveExtractor
                .getBezierCurvePoints(videoInfoDto.getYtpHeatMapPathD());

        return YoutubeBezierCurveDto.builder()
                .videoLength(videoInfoDto.getVideoLength())
                .startPoint(startPoint)
                .bezierCurvePoints(bezierCurvePoints)
                .build();
    }
}
