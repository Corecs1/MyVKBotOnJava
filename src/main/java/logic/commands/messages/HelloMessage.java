package logic.commands.messages;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.messages.Message;
import vk.VKConfig;

public class HelloMessage extends ResponseMessage {

    public HelloMessage(VKConfig config, Message message) throws ClientException, ApiException {
        super(config, message);
    }

    @Override
    public void sendMessage() {
        try {
            sendMessagePattern("Привет, " + getUserInfo().get(0).getFirstName());
        } catch (ClientException | ApiException e) {
            e.printStackTrace();
        }
    }
}
