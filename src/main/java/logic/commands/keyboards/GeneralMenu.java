package logic.commands.keyboards;

import com.vk.api.sdk.objects.messages.*;

import java.util.ArrayList;
import java.util.List;

public class GeneralMenu implements Menu {
    private Keyboard keyboard = new Keyboard();

    @Override
    public Keyboard getKeyboard() {
        createMenu();
        return keyboard;
    }

    private void createMenu() {
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
}
