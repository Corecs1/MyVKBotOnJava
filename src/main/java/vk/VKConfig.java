package vk;

import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class VKConfig {
    private VkApiClient vk;
    private static int ts;
    private GroupActor actor;
    private int maxMsgId = -1;

    // Конструктор инициализации VkApiClient
    public VKConfig() throws ClientException, ApiException {
        loadConfigs();
    }

    private static String setApiKey() {
        String api_key = null;
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("vkconfig.properties"));
            api_key = properties.getProperty("api_key");
        } catch (IOException e) {
            System.out.println("Error load API key");
            e.printStackTrace();
        }
        return api_key;
    }

    // Метод загрузки конфигов
    private void loadConfigs() throws ClientException, ApiException {
        System.out.println("Starting load configs...");
        TransportClient transportClient = HttpTransportClient.getInstance();
        vk = new VkApiClient(transportClient);
        Properties properties = new Properties();
        int groupId;
        String access_token;
        try {
            properties.load(this.getClass().getResourceAsStream("/vkconfig.properties"));
            groupId = Integer.parseInt(properties.getProperty("group_id"));
            access_token = properties.getProperty("picture_access_token");
            actor = new GroupActor(groupId, access_token);
            ts = vk.messages().getLongPollServer(actor).execute().getTs();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Ошибка при загрузке файла конфигурации");
        }
        System.out.println("Configs is load successfully...");
    }

    public VkApiClient getVk() {
        return vk;
    }

    public int getTs() {
        return ts;
    }

    public GroupActor getActor() {
        return actor;
    }

    public int getMaxMsgId() {
        return maxMsgId;
    }

    public static void setTs(int ts) {
        VKConfig.ts = ts;
    }
}
