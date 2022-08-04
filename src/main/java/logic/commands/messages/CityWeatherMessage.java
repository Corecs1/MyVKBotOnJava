package logic.commands.messages;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.messages.Message;
import logic.weather.parser.OpenWeather;
import org.jsoup.HttpStatusException;
import vk.VKConfig;

import java.util.Random;

public class CityWeatherMessage extends ResponseMessage {
    private OpenWeather openWeather;

    public CityWeatherMessage(VKConfig config, Message message) throws ClientException, ApiException {
        super(config, message);
        openWeather = new OpenWeather();
    }

    @Override
    public void sendMessage() throws ClientException, ApiException {
        String userText = getMessage().getText().toLowerCase();
        Random random = new Random();
        String city = userText.substring(7);
        System.out.println(city);
        String weather = "Для города " + city + " информация о погоде отсутствует";
        try {
            weather = openWeather.getWeather(city);
        } catch (HttpStatusException e) {
            e.printStackTrace();
        }
        getConfig().getVk().messages()
                .send(getConfig().getActor())
                .message(weather) // тут я поменял на метод из класса WeatherApi
                .userId(getMessage().getFromId())
                .randomId(random.nextInt(10000))
                .execute();
    }
}
