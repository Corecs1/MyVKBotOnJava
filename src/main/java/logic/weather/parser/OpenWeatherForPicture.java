package logic.weather.parser;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import logic.weather.svgPictures.WeatherPicture;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Properties;

public class OpenWeatherForPicture {
    private static final String apiKey = setApiKey();
    private final String city;
    private static final double CONVERSION_COEFFICIENT = 0.75;

    public OpenWeatherForPicture(String city) {
        this.city = city.replace(' ', '+');
    }

    public void getWeather() throws RuntimeException {
        try {
            String text = getWeatherInfo();
            System.out.println(text);

            JsonParser parser = new JsonParser();
            JsonObject jsonObject = (JsonObject) parser.parse(text);

            JsonObject mainObject = (JsonObject) jsonObject.get("main");
            JsonElement jsonTemp = mainObject.get("temp");
            double temperature = jsonTemp.getAsDouble();
            JsonElement jsonHumidity = mainObject.get("humidity");
            int humidity = jsonHumidity.getAsInt();
            JsonElement jsonPressure = mainObject.get("pressure");
            double pressure = jsonPressure.getAsDouble() * CONVERSION_COEFFICIENT;

            JsonElement jsonCity = jsonObject.get("name");
            String city = jsonCity.getAsString();

            JsonObject jsonSysObject = (JsonObject) jsonObject.get("sys");
            JsonElement jsonCountry = jsonSysObject.get("country");
            String country = jsonCountry.getAsString();

            JsonObject jsonWindObject = (JsonObject) jsonObject.get("wind");
            JsonElement jsonWindSpeed = jsonWindObject.get("speed");
            double windSpeed = jsonWindSpeed.getAsDouble();

            JsonArray weatherArray = jsonObject.getAsJsonArray("weather");
            JsonObject jsonWeatherObject = (JsonObject) weatherArray.get(0);
            JsonElement jsonElementWeather = jsonWeatherObject.get("description");
            String description = jsonElementWeather.getAsString();

            String temperatureString = temperature + "°C";
            String windString = windSpeed + " м/с.";
            String pressureString = pressure + " мм рт.ст.";
            String humidityString = humidity + "%.";

            WeatherPicture weatherPicture = new WeatherPicture(
                    temperatureString,
                    city,
                    country,
                    description,
                    windString,
                    pressureString,
                    humidityString);
            weatherPicture.createPngPicture();
        } catch (RuntimeException e) {
            e.printStackTrace();
            System.out.println("Incorrect city name");
            throw new RuntimeException("К сожалению не удалось получить информацию.\n" + "Проверьте корректность введенного города");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getWeatherInfo() throws IllegalArgumentException {
        String url = String.format("https://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s&lang=ru&units=metric", this.city, apiKey);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();
        String text = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .join();
        return text;
    }

    private static String setApiKey() {
        String api_key = null;
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("src/main/resources/vkconfig.properties"));
            api_key = properties.getProperty("api_key");
        } catch (IOException e) {
            System.out.println("Error load API key");
            e.printStackTrace();
        }
        return api_key;
    }
}
