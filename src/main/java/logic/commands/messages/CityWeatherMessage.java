package logic.commands.messages;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.messages.Message;
import logic.commands.Weather;
import org.jsoup.HttpStatusException;
import vk.VKConfig;

import java.io.IOException;
import java.util.Random;

public class CityWeatherMessage extends ResponseMessage{

    public CityWeatherMessage(VKConfig config, Message message) throws ClientException, ApiException {
        super(config, message);
    }

    @Override
    public void sendMessage() throws ClientException, ApiException {
        String userText = getMessage().getText().toLowerCase();
        Random random = new Random();
        String city = userText.substring(7);
        System.out.println(city);
        Weather weather = null;
        try {
            weather = new Weather(city);
        } catch (HttpStatusException e) {
            getConfig().getVk().messages()
                    .send(getConfig().getActor())
                    .message("Для города " + city + " информация о погоде отсутствует")
                    .userId(getMessage().getFromId())
                    .randomId(random.nextInt(10000))
                    .execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert weather != null;
        try {
            getConfig().getVk().messages()
                    .send(getConfig().getActor())
                    .message(weather.getWeather())
                    .userId(getMessage().getFromId())
                    .randomId(random.nextInt(10000))
                    .execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
