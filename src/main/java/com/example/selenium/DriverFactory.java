package com.example.selenium;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public final class DriverFactory {

    private DriverFactory() {}

    public static WebDriver createDriver() {
        resolveDriverPath();
        ChromeOptions options = new ChromeOptions();
        if (shouldRunHeadless()) {
            options.addArguments("--headless=new", "--no-sandbox",
                    "--disable-dev-shm-usage", "--disable-gpu", "--window-size=1280,900");
        }
        return new ChromeDriver(options);
    }

    private static void resolveDriverPath() {
        String current = System.getProperty("webdriver.chrome.driver");
        if (current != null && !current.trim().isEmpty()) return;

        for (String envVar : new String[]{"WEBDRIVER_CHROME_DRIVER", "CHROMEWEBDRIVER"}) {
            String value = System.getenv(envVar);
            if (value != null && !value.trim().isEmpty()) {
                System.setProperty("webdriver.chrome.driver", value.trim());
                return;
            }
        }
    }

    private static boolean shouldRunHeadless() {
        return "true".equalsIgnoreCase(System.getenv("HEADLESS"))
                || System.getenv("GITHUB_ACTIONS") != null;
    }
}