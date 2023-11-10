package eu.skyrex.countdown.managers;

import eu.skyrex.countdown.Christmas;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;

public interface SelectionMenu {
    void execute(StringSelectInteractionEvent event);

    default void registerSelectionMenu(String name) {
        Christmas.getSelectionMenuManager().register(name, this);
    }
}
