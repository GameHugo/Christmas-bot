package eu.skyrex.countdown.managers;

import eu.skyrex.countdown.Christmas;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;

public interface Modal {
    void execute(ModalInteractionEvent event);

    default void registerModal(String name) {
        Christmas.getModalManager().register(name, this);
    }
}
