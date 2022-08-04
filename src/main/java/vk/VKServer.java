package vk;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import logic.dialog.Chat;
import org.jsoup.HttpStatusException;

public class VKServer {
    public static VKConfig vkConfig;
    private static Chat chat;

    static {
        try {
            vkConfig = new VKConfig();
            chat = new Chat(vkConfig);
        } catch (ApiException | ClientException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws ClientException, ApiException, InterruptedException, HttpStatusException {
        System.out.println("Running server");
        while (true) {
            Thread.sleep(500);
            chat.startChat();
        }
    }
}
