package com.example.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class App {

    private static final String PASSWORD_URL = "https://www.calculator.net/password-generator.html";
    private static final int TIMEOUT_SECONDS = 15;

    public static void main(String[] args) {
        WebDriver driver = DriverFactory.createDriver();
        try {
            System.out.println("=== Задание 1: Генератор паролей ===");
            fetchPassword(driver);

            System.out.println("\n=== Задание 2: Определение IP-адреса ===");
            Task2.fetchIpAddress(driver);

            System.out.println("\n=== Задание 3: Прогноз погоды ===");
            Task3.fetchWeatherForecast(driver);

        } catch (Exception e) {
            System.out.println("Критическая ошибка: " + e.getMessage());
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }

    static void fetchPassword(WebDriver driver) {
        try {
            driver.get(PASSWORD_URL);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT_SECONDS));

            // Нажимаем кнопку генерации, чтобы получить свежий пароль
            WebElement generateBtn = wait.until(
                    ExpectedConditions.elementToBeClickable(
                            By.cssSelector("input[type='submit'][name='submit1']")));
            generateBtn.click();

            // Ждём, пока в поле результата появится непустой текст
            String password = wait.until(d -> {
                try {
                    WebElement bold = d.findElement(
                            By.cssSelector("#resultid .verybigtext b"));
                    String text = bold.getText();
                    return (text != null && !text.trim().isEmpty()) ? text.trim() : null;
                } catch (StaleElementReferenceException | NoSuchElementException ex) {
                    return null;
                }
            });

            System.out.println("Сгенерированный пароль: " + password);

        } catch (Exception e) {
            System.out.println("Ошибка в задании 1: " + e.getMessage());
        }
    }
}