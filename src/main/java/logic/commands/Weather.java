package logic.commands;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;


public class Weather {
    private String city = "samara";
    private Document document;

    public Weather() throws IOException {
        document = Jsoup.connect(String.format("https://world-weather.ru/pogoda/russia/%s/", this.city)).get();
    }

    public Weather(String city) throws IOException {
        Cities cities = new Cities();
        this.city = cities.findCity(city);
    }

    public String getWeather() throws IOException {
        if (city == null) {
            return "К сожалению не удалось найти такого города в базе, либо конструкция запроса была не верной";
        }
        return parse();
    }

    private String parse() throws IOException {
        document = Jsoup.connect(String.format("https://world-weather.ru/pogoda/russia/%s/", city)).get();
        Elements city = document.select("h1");
        Elements description = document.select("span.dw-into");
        Elements nowTemperature = document.select("#weather-now-number");
        String weather = description.text().replaceAll("Подробнее", "\n").replaceAll("Скрыть", "");
        return String.format("%s сейчас %s \n Подробная информация: %s", city.text(), nowTemperature.text(), weather);
    }
}
