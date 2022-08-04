package logic.weather.connect;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Properties;

// todo Перенести класс в OpenWeather
public class CurrentWeatherDataRequest implements WeatherDataRequest {
    private static final String apiKey = setApiKey();
    private final String city;

    public CurrentWeatherDataRequest(String city) {
        this.city = city.replace(' ','+');
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

    @Override
    public String getWeatherInfo(String city) throws IllegalArgumentException {
        String url = String.format("https://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s&lang=ru&units=metric", city, apiKey);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();
        String text = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .join();
        return text;
    }
}
