package logic.weather.parser;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import logic.weather.connect.CurrentWeatherDataRequest;
import logic.weather.connect.WeatherDataRequest;
import org.jsoup.HttpStatusException;

public class OpenWeather {
    public String getWeather(String cityName) throws HttpStatusException {
        String weatherInfo;
        try {
            WeatherDataRequest request = new CurrentWeatherDataRequest(cityName);
            String text = request.getWeatherInfo(cityName);

            System.out.println(text);

            JsonParser parser = new JsonParser();
            JsonObject jsonObject = (JsonObject) parser.parse(text);
            JsonObject mainObject = (JsonObject) jsonObject.get("main");

            JsonElement jsonTemp = mainObject.get("temp");
            Double temperature = jsonTemp.getAsDouble();

            JsonElement jsonPressure = mainObject.get("pressure");
            Double pressure = jsonPressure.getAsDouble();

            JsonElement jsonCity = jsonObject.get("name");
            String city = jsonCity.getAsString();

            JsonArray weatherArray = jsonObject.getAsJsonArray("weather");
            JsonObject jsonWeatherObject = (JsonObject) weatherArray.get(0);
            JsonElement jsonElementWeather = jsonWeatherObject.get("description");
            String description = jsonElementWeather.getAsString();
            System.out.println(city + ", " + temperature + "°C, " + description + " Атмосферное давление " + pressure + " гПa");
            weatherInfo = city + ", " + temperature + "°C, " + description + ", Атмосферное давление: " + pressure + " гПa";
        } catch (RuntimeException e) {
            e.printStackTrace();
            System.out.println("Incorrect city name");
            throw new HttpStatusException("К сожалению не удалось получить информацию.\n"  + "Проверьте корректность введенного города", 404, "");
        }
        return weatherInfo;
    }
}
