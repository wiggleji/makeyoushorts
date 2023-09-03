package com.example.makeyoushorts.util;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;


@Component
public class SeleniumUtil {

    public WebDriver chromeDriver() {
        // set webdriver.chrome.driver with WebDriverManager
        // DOCUMENT: https://bonigarcia.dev/webdrivermanager
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--lang=ko");
        options.addArguments("disable-infobars"); // disable info bars
        options.addArguments("--disable-extensions"); // disable extension
        // sandbox reference: https://chromium.googlesource.com/chromium/src/+/master/docs/design/sandbox.md
        // https://stackoverflow.com/a/50642913/7603684
        options.addArguments("--no-sandbox"); // Bypass OS security model
        options.addArguments("--disable-dev-shm-usage"); // disable limited resource
        options.addArguments("--disable-gpu"); // disable gpu options for browser to render


        return new ChromeDriver(options);
    }

    public static void quit(WebDriver webDriver) {
        if (!ObjectUtils.isEmpty(webDriver)) {
            webDriver.quit();
        }
    }

    public static void closeWindow(WebDriver webDriver) {
        if (!ObjectUtils.isEmpty(webDriver)) {
            webDriver.close();
        }
    }
}
