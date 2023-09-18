package com.example.makeyoushorts.youtube.util;

import com.example.makeyoushorts.util.SeleniumUtil;
import com.example.makeyoushorts.youtube.dto.YoutubeVideoInfoDto;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;

@Slf4j
@Component
public class YoutubeWebClient {
    private final String BASE_URL = "https://www.youtube.com";

    private final WebClient webClient;

    private final SeleniumUtil seleniumUtil;

    public YoutubeWebClient(WebClient webClient, SeleniumUtil seleniumUtil) {
        this.webClient = webClient.mutate()
                .baseUrl(BASE_URL)
                .build();
        this.seleniumUtil = seleniumUtil;
    }

    /**
     * fetch YouTube Heatmap HTML tag from video by ID
     * @param videoId video ID
     * @return return heat-map svg HTML tag String or null if HTML element is not found
     */
    public YoutubeVideoInfoDto fetchYtpHeatMapHtmlByVideoId(String videoId) {
        WebDriver webDriver = seleniumUtil.chromeDriver();
        try {
            webDriver.get(BASE_URL + String.format("/watch?v=%s", videoId));

            // sleep thread to wait heatmap rendering
            Thread.sleep(2000);

            // CSS select heatmap path: `path#ytp-heat-map-path > d`
            String ytpHeatMapPathD = webDriver.findElement(By.className("ytp-heat-map-path")).getAttribute("d");
            log.info(String.format("Youtube video id: %s | ytp-heat-map-path: {%s}", videoId, ytpHeatMapPathD));

            // CSS select video length: `#movie_player > div.ytp-chrome-bottom > div.ytp-chrome-controls > div.ytp-left-controls > div.ytp-time-display.notranslate > span:nth-child(2) > span.ytp-time-duration`
            String videoLengthString = webDriver.findElement(By.className("ytp-time-duration")).getText();
            log.info(String.format("Youtube video id: %s | ytp-time-duration: {%s}", videoId, videoLengthString));

            Integer videoLength = getVideoLength(videoLengthString);

            // quit Selenium
            SeleniumUtil.quit(webDriver);

            return YoutubeVideoInfoDto.builder()
                    .ytpHeatMapPathD(ytpHeatMapPathD)
                    .videoLength(videoLength)
                    .build();

        } catch (NoSuchElementException e) {
            log.warn(String.format("[%s] heat-map element not found | Youtube video id: %s ", e.getClass().toString(), videoId));
        } catch (InterruptedException e) {
            // Thread interrupt handling
            log.error(String.format("[%s] stopped while thread sleep", e.getClass().toString()));
        }
        // quit Selenium
        SeleniumUtil.quit(webDriver);

        return null;
    }

    /*
     * return video length in seconds from String
     * @param videoLengthString: `HH:MM:SS`
     * @return videoLength: video length in seconds
     */
    private int getVideoLength(String videoLengthString) {
        // HH:MM:SS
        // SS(SS) - MM(MM*60) - HH(HH*60*60)
        int videoLength = 0;
        Integer[] times = Arrays.stream(videoLengthString.split(":")).mapToInt(Integer::parseInt).boxed().toArray(Integer[]::new);

        for (int i = times.length-1; i >= 0; i--) {
            videoLength += times[i] * (int) Math.pow(60, times.length - 1 - i);
        }

        return videoLength;
    }
}
