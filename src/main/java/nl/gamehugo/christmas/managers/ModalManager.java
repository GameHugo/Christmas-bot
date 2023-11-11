package nl.gamehugo.christmas.managers;

import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;

import java.util.HashMap;
import java.util.Map;

public class ModalManager {
    private final Map<String, BotModal> modals;

    public ModalManager() {
        modals = new HashMap<>();
    }

    public void register(String name, BotModal botModal) {
        modals.put(name, botModal);
    }

    public boolean execute(ModalInteractionEvent event) {
        try {
            modals.get(event.getModalId()).execute(event);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
