package logic.commands.messages;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.messages.Message;
import com.vk.api.sdk.objects.photos.responses.GetMessagesUploadServerResponse;
import com.vk.api.sdk.objects.photos.responses.MessageUploadResponse;
import com.vk.api.sdk.objects.photos.responses.SaveMessagesPhotoResponse;
import logic.weather.parser.OpenWeather;
import logic.weather.parser.OpenWeatherForPicture;
import vk.VKConfig;

import java.io.File;
import java.util.List;
import java.util.Random;

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
            OpenWeatherForPicture openWeather = new OpenWeatherForPicture(city);
            openWeather.getWeather();
            Random random = new Random();
            File picture = new File("src\\main\\resources\\WeatherCascade.png");

            GetMessagesUploadServerResponse uploadServerResponse = getConfig().getVk().photos().getMessagesUploadServer(getConfig().getActor()).execute();
            MessageUploadResponse messageUploadResponse = getConfig().getVk().upload().photoMessage(uploadServerResponse.getUploadUrl().toString(), picture).execute();
            List<SaveMessagesPhotoResponse> photoList = getConfig()
                    .getVk()
                    .photos()
                    .saveMessagesPhoto(getConfig().getActor(), messageUploadResponse.getPhoto())
                    .server(messageUploadResponse.getServer())
                    .hash(messageUploadResponse.getHash())
                    .execute();
            SaveMessagesPhotoResponse photo = photoList.get(0);
            String attachment = "photo"+photo.getOwnerId() + "_" + photo.getId() + "_" + photo.getAccessKey();
            getConfig().getVk().messages().send(getConfig().getActor()).attachment(attachment).userId(getMessage().getFromId()).randomId(random.nextInt(1000)).execute();

//            OpenWeather openWeather = new OpenWeather(city);
//            weather = openWeather.getWeather();
        } catch (RuntimeException e) {
            e.printStackTrace();
            sendMessagePattern(weather);
        }
    }
}
