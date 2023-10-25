package eu.skyrex.countdown.managers;

import eu.skyrex.countdown.Countdown;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

public interface Button {
    void execute(ButtonInteractionEvent event);

    default void registerButton(String name) {
        Countdown.getButtonManager().register(name, this);
    }
}
