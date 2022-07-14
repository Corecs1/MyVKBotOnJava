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
import org.jsoup.HttpStatusException;
import vk.VKConfig;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Chat {
    private VKConfig config;
    private final VkApiClient vk;
    private int ts;
    private final GroupActor actor;
    private final int maxMsgId = -1;
    private final Random random = new Random();
    private final Keyboard keyboard = new Keyboard();

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
        List<KeyboardButton> line2 = new ArrayList<>();
        line1.add(new KeyboardButton()
                .setAction(new KeyboardButtonAction().setLabel("Привет").setType(TemplateActionTypeNames.TEXT))
                .setColor(KeyboardButtonColor.POSITIVE));
        line1.add(new KeyboardButton()
                .setAction(new KeyboardButtonAction().setLabel("Погода в моём городе").setType(TemplateActionTypeNames.TEXT))
                .setColor(KeyboardButtonColor.POSITIVE));
        line2.add(new KeyboardButton()
                .setAction(new KeyboardButtonAction().setLabel("Погода в другом городе").setType(TemplateActionTypeNames.TEXT))
                .setColor(KeyboardButtonColor.POSITIVE));
        allKey.add(line1);
        allKey.add(line2);
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
                } else if (userText.equals("погода в моём городе")) {
                    if (userCity.equals("null")) {
                        vk.messages()
                                .send(actor)
                                .message("К сожалению информация о городе скрыта в твоём профиле")
                                .userId(userId)
                                .randomId(random.nextInt(10000))
                                .execute();
                    } else {
                        Weather weather = null;
                        try {
                            weather = new Weather(userInfo.get(0).getCity().getTitle());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        assert weather != null;
                        try {
                            vk.messages()
                                    .send(actor)
                                    .message(weather.getWeather())
                                    .userId(userId)
                                    .randomId(random.nextInt(10000))
                                    .execute();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } else if (userText.equals("погода в другом городе")) {
                    vk.messages()
                            .send(actor)
                            .message("Набери сообщение по типу:" + "\n" + "Погода 'Интересующий вас город'" + "\n" + "Например: Погода Москва")
                            .userId(userId)
                            .randomId(random.nextInt(10000))
                            .execute();
                } else if (userText.matches("погода [а-я]+(\\s|-)?[а-я]*")) {
                    String city = userText.substring(7);
                    System.out.println(city);
                    Weather weather = null;
                    try {
                        weather = new Weather(city);
                    } catch (HttpStatusException e) {
                        vk.messages().send(actor).message("Для города " + city + " информация о погоде отсутствует")
                                .userId(message.getFromId())
                                .randomId(random.nextInt(10000))
                                .execute();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    assert weather != null;
                    try {
                        vk.messages()
                                .send(actor)
                                .message(weather.getWeather())
                                .userId(userId)
                                .randomId(random.nextInt(10000))
                                .execute();
                    } catch (IOException e) {
                        e.printStackTrace();
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
    }
}
