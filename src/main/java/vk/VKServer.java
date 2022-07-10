package vk;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;

public class VKServer {
    public static VKConfig vkConfig;
    private static MessageOperation messageOperation;

    static {
        try {
            vkConfig = new VKConfig();
            messageOperation = new MessageOperation(vkConfig);
        } catch (ApiException | ClientException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws ClientException, ApiException, InterruptedException {
        System.out.println("Running server");
        while (true) {
            Thread.sleep(500);
            messageOperation.startChat();
        }
    }
}
