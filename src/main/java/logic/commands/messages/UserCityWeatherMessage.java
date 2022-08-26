package logic.commands.messages;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.messages.Message;
import logic.weather.parser.OpenWeatherForPicture;
import vk.VKConfig;

import java.io.File;

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
            OpenWeatherForPicture openWeather = new OpenWeatherForPicture(getUserInfo().get(0).getCity().getTitle());
            openWeather.getWeather();
            File picture = new File("src\\main\\resources\\WeatherCascade.png");
            sendPicturePattern(picture);
        }
    }
}
