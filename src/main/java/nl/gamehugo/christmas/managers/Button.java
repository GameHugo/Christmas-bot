package nl.gamehugo.christmas.managers;

import nl.gamehugo.christmas.Christmas;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

public interface Button {
    void execute(ButtonInteractionEvent event);

    default void registerButton(String name) {
        Christmas.getButtonManager().register(name, this);
    }
}
