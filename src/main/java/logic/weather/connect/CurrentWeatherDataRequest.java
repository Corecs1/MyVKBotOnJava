package logic.weather.connect;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Properties;

public class CurrentWeatherDataRequest implements WeatherDataRequest {
    private final String apiKey;
    private final String city;

    public CurrentWeatherDataRequest(String city) {
        this.city = city;
        this.apiKey = setApiKey();
    }

    private String setApiKey() {
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
    public String getWeatherInfo() throws IllegalArgumentException {
        String url = String.format("https://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s&lang=ru&units=metric", city, apiKey);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();
        String text = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .join();
        return text;
    }
}
