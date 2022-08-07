package logic.commands.messages;

import com.vk.api.sdk.actions.Upload;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.messages.Message;
import logic.weather.parser.OpenWeather;
import vk.VKConfig;

public class UserCityWeatherMessage extends ResponseMessage {

    public UserCityWeatherMessage(VKConfig config, Message message) throws ClientException, ApiException {
        super(config, message);
    }

    @Override
    public void sendMessage() throws ClientException, ApiException {
        String userCity = String.valueOf(getUserInfo().get(0).getCity());
        if (userCity.equals("null")) {
            sendMessagePattern("К сожалению информация о городе скрыта в твоём профиле");
        } else {
            OpenWeather weather = new OpenWeather(getUserInfo().get(0).getCity().getTitle());
            sendMessagePattern(weather.getWeather());
        }
    }
}
