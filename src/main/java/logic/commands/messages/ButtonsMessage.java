package logic.commands.messages;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.messages.Message;
import logic.commands.keyboards.GeneralMenu;
import logic.commands.keyboards.Menu;
import vk.VKConfig;

import java.util.Random;

public class ButtonsMessage extends ResponseMessage {

    public ButtonsMessage(VKConfig config, Message message) throws ClientException, ApiException {
        super(config, message);
    }

    @Override
    public void sendMessage() throws ClientException, ApiException {
        Menu menu = new GeneralMenu();
        Random random = new Random();
        getConfig().getVk().messages()
                .send(getConfig().getActor())
                .message("Держи кнопки")
                .userId(getMessage().getFromId())
                .randomId(random.nextInt(10000))
                .keyboard(menu.getKeyboard())
                .execute();
    }
}
