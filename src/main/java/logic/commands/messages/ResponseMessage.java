package logic.commands.messages;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.messages.Message;
import com.vk.api.sdk.objects.users.Fields;
import com.vk.api.sdk.objects.users.responses.GetResponse;
import vk.VKConfig;

import java.util.List;
import java.util.Random;

public abstract class ResponseMessage {
    private VKConfig config;
    private Message message;
    private final List<GetResponse> userInfo;

    public ResponseMessage(VKConfig config, Message message) throws ClientException, ApiException {
        this.config = config;
        this.message = message;
        this.userInfo = config.getVk().users()
                .get(config.getActor())
                .userIds(String.valueOf(message.getFromId()))
                .fields(Fields.CITY)
                .execute();
    }

    public VKConfig getConfig() {
        return config;
    }

    public Message getMessage() {
        return message;
    }

    public List<GetResponse> getUserInfo() {
        return userInfo;
    }

    public abstract void sendMessage() throws ClientException, ApiException;

    void sendMessagePattern(String text) throws ClientException, ApiException {
        Random random = new Random();
        config.getVk().messages()
                .send(config.getActor())
                .message(text)
                .userId(message.getFromId())
                .randomId(random.nextInt(10000))
                .execute();
    }
}
