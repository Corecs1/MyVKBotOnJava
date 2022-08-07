package logic.commands.messages;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.messages.Message;
import logic.weather.parser.OpenWeather;
import vk.VKConfig;

public class CityWeatherMessage extends ResponseMessage {

    public CityWeatherMessage(VKConfig config, Message message) throws ClientException, ApiException {
        super(config, message);
    }

    @Override
    public void sendMessage() throws ClientException, ApiException {
        String userText = getMessage().getText().toLowerCase();
        String city = userText.substring(7);
        System.out.println(city);
        String weather = "Для города " + city + " информация о погоде отсутствует\nПроверьте корректность введенного города";
        try {
            OpenWeather openWeather = new OpenWeather(city);
            weather = openWeather.getWeather();
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        sendMessagePattern(weather);
    }
}
