package logic.commands.messages;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.messages.Message;
import logic.commands.Weather;
import vk.VKConfig;

import java.io.IOException;

public class UserCityWeatherMessage extends ResponseMessage{

    public UserCityWeatherMessage(VKConfig config, Message message) throws ClientException, ApiException {
        super(config, message);
    }

    @Override
    public void sendMessage() throws ClientException, ApiException {
        String userCity = String.valueOf(getUserInfo().get(0).getCity());
        if (userCity.equals("null")) {
            sendMessagePattern("К сожалению информация о городе скрыта в твоём профиле");
        } else {
            Weather weather = null;
            try {
                weather = new Weather(getUserInfo().get(0).getCity().getTitle());
            } catch (IOException e) {
                e.printStackTrace();
            }
            assert weather != null;
            try {
               sendMessagePattern(weather.getWeather());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
