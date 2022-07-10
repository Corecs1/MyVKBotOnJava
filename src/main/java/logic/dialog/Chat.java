package logic.dialog;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.messages.*;
import com.vk.api.sdk.objects.users.Fields;
import com.vk.api.sdk.objects.users.responses.GetResponse;
import com.vk.api.sdk.queries.messages.MessagesGetLongPollHistoryQuery;
import logic.commands.Weather;
import vk.VKConfig;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Chat {
    private VKConfig config;
    private VkApiClient vk;
    private int ts;
    private GroupActor actor;
    private int maxMsgId = -1;
    private Random random = new Random();
    Keyboard keyboard = new Keyboard();

    public Chat(VKConfig config) {
        this.config = config;
        this.vk = config.getVk();
        this.ts = config.getTs();
        this.actor = config.getActor();
        keyboard();
    }

    private void keyboard() {
        List<List<KeyboardButton>> allKey = new ArrayList<>();
        List<KeyboardButton> line1 = new ArrayList<>();
        line1.add(new KeyboardButton()
                .setAction(new KeyboardButtonAction().setLabel("Привет").setType(TemplateActionTypeNames.TEXT))
                .setColor(KeyboardButtonColor.POSITIVE));
        line1.add(new KeyboardButton()
                .setAction(new KeyboardButtonAction().setLabel("Город").setType(TemplateActionTypeNames.TEXT))
                .setColor(KeyboardButtonColor.POSITIVE));
        line1.add(new KeyboardButton()
                .setAction(new KeyboardButtonAction().setLabel("Погода").setType(TemplateActionTypeNames.TEXT))
                .setColor(KeyboardButtonColor.POSITIVE));
        allKey.add(line1);
        keyboard.setButtons(allKey);
    }

    public void startChat() throws ClientException, ApiException {
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

                switch (userText) {
                    case "hello":
                    case "привет":
                        vk.messages()
                                .send(actor)
                                .message("Привет, " + userFirstName)
                                .userId(userId)
                                .randomId(random.nextInt(10000))
                                .execute();
                        break;
                    case "кнопки":
                        vk.messages()
                                .send(actor)
                                .message("Держи кнопки")
                                .userId(message.getFromId())
                                .randomId(random.nextInt(10000))
                                .keyboard(keyboard)
                                .execute();
                        break;
                    case "город":
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
                        break;
                    case "погода":
                        Weather weather = null;
                        try {
                            weather = new Weather();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        vk.messages()
                                .send(actor)
                                .message(weather.getWeather())
                                .userId(userId)
                                .randomId(random.nextInt(10000))
                                .execute();
                        break;
                    default:
                        vk.messages().send(actor).message("Я не понимаю тебя:(" + "\n" + "Попробуй введи слово 'Кнопки' и воспользуйся их функционалом")
                                .userId(message.getFromId())
                                .randomId(random.nextInt(10000))
                                .execute();
                        break;
                }
                ts = vk.messages().getLongPollServer(actor).execute().getTs();
            }
        }
    }
}
