package nl.gamehugo.christmas.managers;

import nl.gamehugo.christmas.Christmas;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public interface Command {
    String getName();
    String getDescription();

    void execute(SlashCommandInteractionEvent event);

    default void registerCommand(String name) {
        Christmas.getCommandManager().register(this);
    }
}
