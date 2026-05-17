package com.example.selenium;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public final class PageJsonFetcher {

    private static final int TIMEOUT_SECONDS = 15;

    private PageJsonFetcher() {}

    public static JSONObject fetch(WebDriver driver, String url) throws Exception {
        driver.get(url);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT_SECONDS));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("pre")));
        String rawJson = driver.findElement(By.tagName("pre")).getText();
        return (JSONObject) new JSONParser().parse(rawJson);
    }
}