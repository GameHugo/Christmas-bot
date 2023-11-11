package nl.gamehugo.christmas.managers;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

import java.util.HashMap;
import java.util.Map;

public class ButtonManager {
    private final Map<String, BotButton> buttons;

    public ButtonManager() {
        buttons = new HashMap<>();
    }

    public void register(String name, BotButton botButton) {
        buttons.put(name, botButton);
    }

    public boolean execute(ButtonInteractionEvent event) {
        try {
            buttons.get(event.getComponentId()).execute(event);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
