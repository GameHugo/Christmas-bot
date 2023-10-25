package eu.skyrex.countdown.managers;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

import java.util.HashMap;
import java.util.Map;

public class ButtonManager {
    private final Map<String, Button> buttons;

    public ButtonManager() {
        buttons = new HashMap<>();
    }

    public void register(String name, Button button) {
        buttons.put(name, button);
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
