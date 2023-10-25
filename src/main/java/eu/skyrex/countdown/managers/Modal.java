package eu.skyrex.countdown.managers;

import eu.skyrex.countdown.Countdown;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;

public interface Modal {
    void execute(ModalInteractionEvent event);

    default void registerModal(String name) {
        Countdown.getModalManager().register(name, this);
    }
}
