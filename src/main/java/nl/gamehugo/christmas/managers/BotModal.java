package nl.gamehugo.christmas.managers;

import nl.gamehugo.christmas.Christmas;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;

public interface BotModal {
    void execute(ModalInteractionEvent event);

    default void registerModal(String name) {
        Christmas.getModalManager().register(name, this);
    }
}
