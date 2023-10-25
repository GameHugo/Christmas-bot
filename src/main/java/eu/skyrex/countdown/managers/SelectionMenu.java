package eu.skyrex.countdown.managers;

import eu.skyrex.countdown.Countdown;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;

public interface SelectionMenu {
    void execute(StringSelectInteractionEvent event);

    default void registerSelectionMenu(String name) {
        Countdown.getSelectionMenuManager().register(name, this);
    }
}
