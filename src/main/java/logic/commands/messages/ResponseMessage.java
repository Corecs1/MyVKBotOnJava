package logic.commands.messages;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.messages.Message;
import com.vk.api.sdk.objects.photos.responses.GetMessagesUploadServerResponse;
import com.vk.api.sdk.objects.photos.responses.MessageUploadResponse;
import com.vk.api.sdk.objects.photos.responses.SaveMessagesPhotoResponse;
import com.vk.api.sdk.objects.users.Fields;
import com.vk.api.sdk.objects.users.responses.GetResponse;
import vk.VKConfig;

import java.io.File;
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

    void sendPicturePattern(File picture) throws ClientException, ApiException {
        Random random = new Random();
        GetMessagesUploadServerResponse uploadServerResponse = getConfig().getVk().photos().getMessagesUploadServer(getConfig().getActor()).execute();
        MessageUploadResponse messageUploadResponse = getConfig().getVk().upload().photoMessage(uploadServerResponse.getUploadUrl().toString(), picture).execute();
        picture.delete();
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
    }
}
