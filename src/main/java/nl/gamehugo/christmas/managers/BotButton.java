package nl.gamehugo.christmas.managers;

import nl.gamehugo.christmas.Christmas;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

public interface BotButton {
    void execute(ButtonInteractionEvent event);

    default void registerButton(String name) {
        Christmas.getButtonManager().register(name, this);
    }
    default void registerButtons(String... names) {
        for(String name : names) {
            registerButton(name);
        }
    }
}
