package logic.commands.messages;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.messages.Message;
import vk.VKConfig;

public class UnknownMessage extends ResponseMessage{

    public UnknownMessage(VKConfig config, Message message) throws ClientException, ApiException {
        super(config, message);
    }

    @Override
    public void sendMessage() throws ClientException, ApiException {
        sendMessagePattern("Я не понимаю тебя:(" + "\n" + "Попробуй введи слово 'Кнопки' и воспользуйся их функционалом");
    }
}
