package com.example.selenium;

import org.json.simple.JSONObject;
import org.openqa.selenium.WebDriver;

public final class Task2 {

    private static final String IPIFY_URL = "https://api.ipify.org/?format=json";

    private Task2() {}

    public static void fetchIpAddress(WebDriver driver) {
        try {
            JSONObject response = PageJsonFetcher.fetch(driver, IPIFY_URL);
            String ip = (String) response.get("ip");
            System.out.println("Внешний IPv4-адрес: " + ip);
        } catch (Exception e) {
            System.out.println("Ошибка в задании 2: " + e.getMessage());
        }
    }
}