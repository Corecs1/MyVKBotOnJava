package logic.dialog;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.messages.Message;
import com.vk.api.sdk.objects.users.Fields;
import com.vk.api.sdk.objects.users.responses.GetResponse;
import com.vk.api.sdk.queries.messages.MessagesGetLongPollHistoryQuery;
import logic.commands.CommandsFactory;
import logic.commands.messages.MessageTypes;
import org.jsoup.HttpStatusException;
import vk.VKConfig;

import java.util.List;

public class Chat {
    private VKConfig config;
    private MessagesGetLongPollHistoryQuery historyQuery;
    private List<Message> messages;

    public Chat(VKConfig config) {
        this.config = config;
    }

    private void initMessages() throws ClientException, ApiException {
        historyQuery = config
                .getVk()
                .messages()
                .getLongPollHistory(config.getActor())
                .ts(config.getTs());
        messages = historyQuery
                .execute()
                .getMessages().getItems();
    }

    private void logging(Message message) throws ClientException, ApiException {
        System.out.println(message.getText());
        List<GetResponse> userInfo = config.getVk().users()
                .get(config.getActor())
                .userIds(String.valueOf(message.getFromId()))
                .fields(Fields.CITY)
                .execute();
        System.out.println(userInfo);
    }

    public void startChat() throws ClientException, ApiException, HttpStatusException {
        initMessages();
        if (!messages.isEmpty()) {
            for (Message message : messages) {
                CommandsFactory commandsFactory = new CommandsFactory(config, message);
                String userText = message.getText().toLowerCase();

                logging(message);
                if (userText.equals("hello") || userText.equals("привет")) {
                    commandsFactory.getMessage(MessageTypes.HELLO).sendMessage();
                } else if (userText.equals("кнопки")) {
                    commandsFactory.getMessage(MessageTypes.BUTTONS).sendMessage();
                } else if (userText.equals("погода в моём городе")) {
                    commandsFactory.getMessage(MessageTypes.USER_CITY_WEATHER).sendMessage();
                } else if (userText.equals("погода в другом городе")) {
                    commandsFactory.getMessage(MessageTypes.INFO).sendMessage();
                } else if (userText.matches("погода [а-я]+(\\s|-)?[а-я]*")) {
                    commandsFactory.getMessage(MessageTypes.CITY_WEATHER).sendMessage();
                } else {
                    commandsFactory.getMessage(MessageTypes.UNKNOWN).sendMessage();
                }
                VKConfig.setTs(config.getVk().messages().getLongPollServer(config.getActor()).execute().getTs());
            }
        }
    }
}
