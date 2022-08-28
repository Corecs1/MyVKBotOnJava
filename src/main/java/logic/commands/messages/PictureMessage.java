package logic.commands.messages;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.messages.Message;
import com.vk.api.sdk.objects.photos.responses.GetMessagesUploadServerResponse;
import com.vk.api.sdk.objects.photos.responses.MessageUploadResponse;
import com.vk.api.sdk.objects.photos.responses.SaveMessagesPhotoResponse;
import vk.VKConfig;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Random;

public class PictureMessage extends ResponseMessage{

    public PictureMessage(VKConfig config, Message message) throws ClientException, ApiException {
        super(config, message);
    }

    @Override
    public void sendMessage() throws ClientException, ApiException {
        Random random = new Random();
        File picture = new File("src\\main\\resources\\helloWorld.jpg");

        GetMessagesUploadServerResponse uploadServerResponse = getConfig().getVk().photos().getMessagesUploadServer(getConfig().getActor()).execute();
        MessageUploadResponse messageUploadResponse = getConfig().getVk().upload().photoMessage(uploadServerResponse.getUploadUrl().toString(), picture).execute();
        List<SaveMessagesPhotoResponse> photoList = getConfig()
                .getVk()
                .photos()
                .saveMessagesPhoto(getConfig().getActor(), messageUploadResponse.getPhoto())
                .server(messageUploadResponse.getServer())
                .hash(messageUploadResponse.getHash())
                .execute();
        System.out.println("1 " + photoList);
        SaveMessagesPhotoResponse photo = photoList.get(0);
        System.out.println("2 " + photo);
        String attachment = "photo"+photo.getOwnerId() + "_" + photo.getId() + "_" + photo.getAccessKey();
        System.out.println("3 " + attachment);
        getConfig().getVk().messages().send(getConfig().getActor()).attachment(attachment).userId(getMessage().getFromId()).randomId(random.nextInt(1000)).execute();
    }
}
