package vk;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import logic.dialog.ChatNew;

public class VKServerTest {
    public static VKConfig vkConfig;
    private static ChatNew chat;

    static {
        try {
            vkConfig = new VKConfig();
            chat = new ChatNew(vkConfig);
        } catch (ApiException | ClientException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws ClientException, ApiException, InterruptedException {
        System.out.println("Running server");
        while (true) {
            Thread.sleep(500);
            chat.startChat();
        }
    }
}
