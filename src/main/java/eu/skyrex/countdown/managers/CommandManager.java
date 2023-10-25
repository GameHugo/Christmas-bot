package eu.skyrex.countdown.managers;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.HashMap;
import java.util.Map;

public class CommandManager {
    private final Map<String, Command> commands;

    public CommandManager() {
        commands = new HashMap<>();
    }

    public void register(String name, Command command) {
        commands.put(name, command);
    }

    public boolean execute(SlashCommandInteractionEvent event) {
        try {
            commands.get(event.getName()).execute(event);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
