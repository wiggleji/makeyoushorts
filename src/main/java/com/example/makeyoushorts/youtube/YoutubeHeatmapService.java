package com.example.makeyoushorts.youtube;

import com.example.makeyoushorts.youtube.dto.YoutubeBezierCurveDto;
import com.example.makeyoushorts.youtube.exception.HeatMapNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Slf4j
@Service
public class YoutubeHeatmapService {

    private final YoutubeWebClient youtubeWebClient;
    private final CubicBezierCurveExtractor cubicBezierCurveExtractor;

    public YoutubeHeatmapService(YoutubeWebClient youtubeWebClient, CubicBezierCurveExtractor cubicBezierCurveExtractor) {
        this.youtubeWebClient = youtubeWebClient;
        this.cubicBezierCurveExtractor = cubicBezierCurveExtractor;
    }

    public YoutubeBezierCurveDto getYouTubeVideoBezierCurvePoints(String videoId) {
        String ytpHeatMapHtml = youtubeWebClient.fetchYtpHeatMapHtmlByVideoId(videoId);

        if (ytpHeatMapHtml == null)
            // throws HeatMapNotFoundException if no heatmap is found
            throw new HeatMapNotFoundException(String.format("Failed to get heatmap from Youtube| ID: %s", videoId));

        ArrayList<Float> startPoint = cubicBezierCurveExtractor.getBezierStartPoint(ytpHeatMapHtml);
        ArrayList<ArrayList<ArrayList<Float>>> bezierCurvePoints = cubicBezierCurveExtractor.getBezierCurvePoints(ytpHeatMapHtml);

        return YoutubeBezierCurveDto.builder()
                .startPoint(startPoint)
                .bezierCurvePoints(bezierCurvePoints)
                .build();
    }
}
