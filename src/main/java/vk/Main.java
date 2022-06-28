package vk;

import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.messages.*;
import com.vk.api.sdk.objects.users.Fields;
import com.vk.api.sdk.objects.users.responses.GetResponse;
import com.vk.api.sdk.queries.messages.MessagesGetLongPollHistoryQuery;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;

public class Main {
    private static VkApiClient vk;
    private static int ts;
    private static GroupActor actor;
    private int maxMsgId = -1;

    private static void loadConfigs() throws ClientException, ApiException {
        System.out.println("Starting load configs...");
        Properties properties = new Properties();
        int groupId;
        String access_token;
        try {
            TransportClient transportClient = HttpTransportClient.getInstance();
            vk = new VkApiClient(transportClient);
            properties.load(new FileInputStream("src/main/resources/vkconfig.properties"));
            groupId = Integer.parseInt(properties.getProperty("group_id"));
            access_token = properties.getProperty("access_token");
            actor = new GroupActor(groupId, access_token);
            ts = vk.messages().getLongPollServer(actor).execute().getTs();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Ошибка при загрузке файла конфигурации");
        }
        System.out.println("Configs is load successfully...");
    }

    public static void main(String[] args) throws Exception {
        Keyboard keyboard = new Keyboard();
        Random random = new Random();
        loadConfigs();
        ts = vk.messages().getLongPollServer(actor).execute().getTs();
        System.out.println("Server is started...");

        List<List<KeyboardButton>> allKey = new ArrayList<>();
        List<KeyboardButton> line1 = new ArrayList<>();
        line1.add(new KeyboardButton()
                .setAction(new KeyboardButtonAction().setLabel("Привет").setType(TemplateActionTypeNames.TEXT))
                .setColor(KeyboardButtonColor.POSITIVE));
        line1.add(new KeyboardButton()
                .setAction(new KeyboardButtonAction().setLabel("Город").setType(TemplateActionTypeNames.TEXT))
                .setColor(KeyboardButtonColor.DEFAULT));
        allKey.add(line1);
        keyboard.setButtons(allKey);

        while (true) {
// System.out.println("ts = " + ts);
            MessagesGetLongPollHistoryQuery historyQuery = vk.messages().getLongPollHistory(actor).ts(ts);
            List<Message> messages = historyQuery.execute().getMessages().getItems();
            if (!messages.isEmpty()) {
                for (Message message : messages) {
                    System.out.println(message.getText());
                    int userId = message.getFromId();
                    List<GetResponse> userInfo = vk.users()
                            .get(actor)
                            .userIds(String.valueOf(userId))
                            .fields(Fields.CITY).execute();
                    System.out.println(userInfo);
                    String userText = message.getText().toLowerCase();
                    String userFirstName = userInfo.get(0).getFirstName();
                    String userLastName = userInfo.get(0).getLastName();
                    String userCity = String.valueOf(userInfo.get(0).getCity());
                    System.out.println(userCity);

                    if (userText.equals("hello") || userText.equals("привет")) {
                        vk.messages()
                                .send(actor)
                                .message("Привет, " + userFirstName)
                                .userId(userId)
                                .randomId(random.nextInt(10000))
                                .execute();
                    } else if (userText.equals("кнопки")) {
                        vk.messages()
                                .send(actor)
                                .message("Держи кнопки")
                                .userId(message.getFromId())
                                .randomId(random.nextInt(10000))
                                .keyboard(keyboard)
                                .execute();
                    } else if (userText.equals("город")) {
                        if (userCity.equals("null")) {
                            vk.messages()
                                    .send(actor)
                                    .message("К сожалению информация о городе скрыта в твоём профиле")
                                    .userId(userId)
                                    .randomId(random.nextInt(10000))
                                    .execute();
                        } else {
                            vk.messages()
                                    .send(actor)
                                    .message("Твой город: " + userInfo.get(0).getCity().getTitle())
                                    .userId(userId)
                                    .randomId(random.nextInt(10000))
                                    .execute();
                        }
                    } else {
                        vk.messages().send(actor).message("Я не понимаю тебя:(" + "\n" + "Попробуй введи слово 'Кнопки' и воспользуйся их функционалом")
                                .userId(message.getFromId())
                                .randomId(random.nextInt(10000))
                                .execute();
                    }
                    ts = vk.messages().getLongPollServer(actor).execute().getTs();
                }
            }
            Thread.sleep(500);
        }
    }
}