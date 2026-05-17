package com.example.selenium;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.openqa.selenium.WebDriver;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public final class Task3 {

    private static final String METEO_URL =
            "https://api.open-meteo.com/v1/forecast"
            + "?latitude=56&longitude=44"
            + "&hourly=temperature_2m,rain"
            + "&current=cloud_cover"
            + "&timezone=Europe%2FMoscow"
            + "&forecast_days=1"
            + "&wind_speed_unit=ms";

    private static final DateTimeFormatter INPUT_FMT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
    private static final DateTimeFormatter OUTPUT_FMT =
            DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    private Task3() {}

    public static void fetchWeatherForecast(WebDriver driver) {
        fetchWeatherForecast(driver, Paths.get("result", "forecast.txt"));
    }

    public static void fetchWeatherForecast(WebDriver driver, Path outputFile) {
        try {
            JSONObject root = PageJsonFetcher.fetch(driver, METEO_URL);
            JSONObject hourly = (JSONObject) root.get("hourly");
            JSONArray times = (JSONArray) hourly.get("time");
            JSONArray temps  = (JSONArray) hourly.get("temperature_2m");
            JSONArray rains  = (JSONArray) hourly.get("rain");

            JSONObject units = (JSONObject) root.get("hourly_units");
            String tempUnit  = (String) units.get("temperature_2m");

            String table = buildTable(times, temps, rains, tempUnit);

            System.out.print(table);

            if (outputFile.getParent() != null) {
                Files.createDirectories(outputFile.getParent());
            }
            Files.write(outputFile, table.getBytes(StandardCharsets.UTF_8));
            System.out.println("Таблица сохранена в: " + outputFile.toAbsolutePath());

        } catch (Exception e) {
            System.out.println("Ошибка в задании 3: " + e.getMessage());
        }
    }

    private static String buildTable(JSONArray times, JSONArray temps,
                                     JSONArray rains, String tempUnit) {
        int rows = times.size();

        String[] headers = {"№", "Дата/время", "Температура (" + tempUnit + ")", "Осадки (мм)"};

        int[] widths = new int[headers.length];
        for (int c = 0; c < headers.length; c++) {
            widths[c] = headers[c].length();
        }
        for (int i = 0; i < rows; i++) {
            widths[0] = Math.max(widths[0], String.valueOf(i + 1).length());
            widths[1] = Math.max(widths[1], formatDateTime((String) times.get(i)).length());
            widths[2] = Math.max(widths[2], String.valueOf(temps.get(i)).length());
            widths[3] = Math.max(widths[3], String.valueOf(rains.get(i)).length());
        }

        String separator = buildSeparator(widths);
        StringBuilder sb = new StringBuilder();

        sb.append(separator).append('\n');
        sb.append(buildRow(headers, widths, new boolean[]{false, false, true, true})).append('\n');
        sb.append(separator).append('\n');

        for (int i = 0; i < rows; i++) {
            String[] cells = {
                String.valueOf(i + 1),
                formatDateTime((String) times.get(i)),
                String.valueOf(temps.get(i)),
                String.valueOf(rains.get(i))
            };
            sb.append(buildRow(cells, widths, new boolean[]{true, false, true, true})).append('\n');
        }

        sb.append(separator).append('\n');
        return sb.toString();
    }

    private static String buildSeparator(int[] widths) {
        StringBuilder sb = new StringBuilder("+");
        for (int w : widths) {
            sb.append("-".repeat(w + 2)).append("+");
        }
        return sb.toString();
    }

    private static String buildRow(String[] cells, int[] widths, boolean[] rightAlign) {
        StringBuilder sb = new StringBuilder("|");
        for (int c = 0; c < cells.length; c++) {
            String fmt = rightAlign[c]
                    ? " %" + widths[c] + "s |"
                    : " %-" + widths[c] + "s |";
            sb.append(String.format(Locale.US, fmt, cells[c]));
        }
        return sb.toString();
    }

    private static String formatDateTime(String iso) {
        try {
            return LocalDateTime.parse(iso, INPUT_FMT).format(OUTPUT_FMT);
        } catch (Exception e) {
            return iso;
        }
    }
}