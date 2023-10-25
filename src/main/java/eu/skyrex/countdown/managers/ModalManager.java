package eu.skyrex.countdown.managers;

import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;

import java.util.HashMap;
import java.util.Map;

public class ModalManager {
    private final Map<String, Modal> modals;

    public ModalManager() {
        modals = new HashMap<>();
    }

    public void register(String name, Modal modal) {
        modals.put(name, modal);
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
