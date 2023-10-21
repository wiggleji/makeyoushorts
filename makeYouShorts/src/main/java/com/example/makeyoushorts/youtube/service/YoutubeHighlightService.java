package com.example.makeyoushorts.youtube.service;

import com.example.makeyoushorts.youtube.dto.YoutubeBezierCurveDto;
import com.example.makeyoushorts.youtube.dto.YoutubeTopFiveHighlightsDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class YoutubeHighlightService {

    private final YoutubeHeatmapService heatmapService;

    public YoutubeHighlightService(YoutubeHeatmapService youtubeHeatmapService) {
        this.heatmapService = youtubeHeatmapService;
    }

    public YoutubeTopFiveHighlightsDto retrieveTopFiveHighlightYoutubeVideo(String videoId) {
        YoutubeBezierCurveDto curveDto = heatmapService.getYoutubeBezierCurveDtoFromYoutubeVideo(videoId);
        List<ArrayList<ArrayList<Float>>> topFiveBezier = getTopFiveHighlightBezierCubic(curveDto);

        return YoutubeTopFiveHighlightsDto.builder()
                .topHighlights(getTopFiveHighlightSeconds(topFiveBezier, curveDto.getVideoLength()))
                .build();
    }

    /*
     * return top five highlight seconds from video
     * @param highlightsBezier top five highlight bezierPoints from YoutubeHighlightService.getTopFiveHighlightBezierCubic
     * @return List of highlight seconds[from, to]
     */
    private ArrayList<ArrayList<Integer>> getTopFiveHighlightSeconds(List<ArrayList<ArrayList<Float>>> highlightsBezier,
                                                                    Integer videoLength) {
        ArrayList<ArrayList<Integer>> result = new ArrayList<>();

        highlightsBezier.forEach(bezierCurve -> {
            Float topPointSecondPercentage = bezierCurve.get(0).get(0);

            // startPointPercentage: topPoint - 10
            Integer startPointSeconds = (int) (videoLength * ((topPointSecondPercentage - 10) / 1000));
            // endPointPercentage: topPoint + 10
            Integer endPointSeconds = (int) (videoLength * ((topPointSecondPercentage + 10) / 1000));

            if (topPointSecondPercentage < 10) {
                // if topPointSecondPercentage less than 10,
                // set startPointSeconds as 00:00
                result.add(new ArrayList<>(Arrays.asList(0, endPointSeconds)));
                return;
            }
            if (topPointSecondPercentage > 990) {
                // if topPointSecondPercentage larger than 990
                // set endPointSeconds as end of video
                result.add(new ArrayList<>(Arrays.asList(startPointSeconds, 1000)));
                return;
            }

            result.add(new ArrayList<>(Arrays.asList(startPointSeconds, endPointSeconds)));
        });

        return result;
    }

    /*
     * return top five highlight bezierPoints from video
     * @param curveDto YoutubeBezierCurveDto from YoutubeHeatmapService
     * @return List of highlight bezierPoints
     */
    private List<ArrayList<ArrayList<Float>>> getTopFiveHighlightBezierCubic(YoutubeBezierCurveDto curveDto) {
        ArrayList<ArrayList<ArrayList<Float>>> curvePoints = curveDto.getBezierCurvePoints();
        Integer videoLength = curveDto.getVideoLength();

        curveDto.bezierCurvePoints.sort((a, b) -> Float.compare(a.get(a.size()-1).get(1), b.get(b.size()-1).get(1)));

        return curvePoints.subList(0, 5);
    }
}
