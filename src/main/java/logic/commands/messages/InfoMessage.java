package logic.commands.messages;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.messages.Message;
import vk.VKConfig;

public class InfoMessage extends ResponseMessage{

    public InfoMessage(VKConfig config, Message message) throws ClientException, ApiException {
        super(config, message);
    }

    @Override
    public void sendMessage() throws ClientException, ApiException {
        sendMessagePattern("Набери сообщение по типу:" + "\n" + "Погода 'Интересующий вас город'" + "\n" + "Например: Погода Москва");
    }
}
