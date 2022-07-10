package logic.commands;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;


public class Weather {
    private String city = "samara";
    private Document document;

    public Weather() throws IOException {
        document = Jsoup.connect(String.format("https://world-weather.ru/pogoda/russia/%s/", city)).get();
    }
    Weather(String city) {
        this.city = city;
        document = (Document) Jsoup.connect(String.format("https://world-weather.ru/pogoda/russia/%s/",city));
    }

    public String getWeather() {
        Elements city = document.select("h1");
        Elements description = document.select("span.dw-into");
        Elements nowTemperature = document.select("#weather-now-number");
        String weather = description.text().replaceAll("Подробнее", "\n").replaceAll("Скрыть", "");
        String allWeatherData = String.format("%s сейчас %s \n Подробная информация: %s", city.text(), nowTemperature.text(), weather);
        return allWeatherData;
    }
}
