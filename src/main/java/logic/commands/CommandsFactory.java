package logic.commands;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.messages.Message;
import logic.commands.messages.*;
import vk.VKConfig;

public class CommandsFactory {
    private final VKConfig config;
    private final Message message;

    public CommandsFactory(VKConfig config, Message message) throws ClientException, ApiException {
        this.config = config;
        this.message = message;
    }

    public ResponseMessage getMessage(MessageTypes type) throws ClientException, ApiException {
        ResponseMessage toReturn = null;
        switch (type) {
            case HELLO:
                toReturn = new HelloMessage(config, message);
                break;
            case BUTTONS:
                toReturn = new ButtonsMessage(config, message);
                break;
            case CITY_WEATHER:
                toReturn = new CityWeatherMessage(config, message);
                break;
            case USER_CITY_WEATHER:
                toReturn = new UserCityWeatherMessage(config, message);
                break;
            case UNKNOWN:
                toReturn = new UnknownMessage(config, message);
                break;
            case INFO:
                toReturn = new InfoMessage(config, message);
                break;
            case GET_PICTURE:
                toReturn = new PictureMessage(config, message);
                break;
            default:
                throw new IllegalArgumentException("Wrong message type " + type);
        }
        return toReturn;
    }
}
