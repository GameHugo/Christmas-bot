package eu.skyrex.countdown.managers;

import eu.skyrex.countdown.Countdown;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public interface Command {
    void execute(SlashCommandInteractionEvent event);

    default void registerCommand(String name) {
        Countdown.getCommandManager().register(name, this);
    }
}
