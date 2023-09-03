package com.example.makeyoushorts.youtube;

import com.example.makeyoushorts.util.SeleniumUtil;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

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
    public String fetchYtpHeatMapHtmlByVideoId(String videoId) {
        WebDriver webDriver = seleniumUtil.chromeDriver();
        try {
            webDriver.get(BASE_URL + String.format("/watch?v=%s", videoId));

            // sleep thread to wait heatmap rendering
            Thread.sleep(2000);

            // CSS select heatmap path: `path#ytp-heat-map-path > d`
            String ytpHeatMapPathD = webDriver.findElement(By.className("ytp-heat-map-path")).getAttribute("d");
            log.info(String.format("Youtube video id: %s | ytp-heat-map-path: {%s}", videoId, ytpHeatMapPathD));

            // quit Selenium
            SeleniumUtil.quit(webDriver);

            return ytpHeatMapPathD;

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
}
