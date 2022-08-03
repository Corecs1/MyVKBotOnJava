package logic.weather.parser;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import logic.weather.connect.CurrentWeatherDataRequest;
import logic.weather.connect.WeatherDataRequest;

public class WeatherParser {
    private String city;

    public WeatherParser(String city) {
        this.city = city;
    }

    public String getWeather() {
        String weatherInfo = null;
        try {
            WeatherDataRequest request = new CurrentWeatherDataRequest(city);
            String text = request.getWeatherInfo();

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
            System.out.println(city + ", " + temperature + "°C, " + description + " Атмосферное давление " + pressure + " hPa");
            weatherInfo = city + ", " + temperature + "°C, " + description + ", Атмосферное давление: " + pressure + " hPa";
        } catch (RuntimeException e) {
            e.printStackTrace();
            System.out.println("Incorrect city name");
            weatherInfo = ("К сожалению не удалось получить информацию.\n"  + "Проверьте корректность введенного города") ;
        }
        return weatherInfo;
    }
}
