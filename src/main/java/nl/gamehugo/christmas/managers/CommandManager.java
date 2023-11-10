package nl.gamehugo.christmas.managers;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.util.HashMap;
import java.util.Map;

public class CommandManager {
    JDA jda;
    private final Map<String, Command> commands;

    public CommandManager(JDA jda) {
        this.jda = jda;
        commands = new HashMap<>();
    }

    public CommandManager() {
        commands = new HashMap<>();
    }

    /**
     * Registers a command to the command manager. This does not register the command to Discord...
     * @param command The command to register
     */
    public void register(Command command) {
        if(command == null) throw new IllegalArgumentException("Command cannot be null");
        if(command.getName() == null) throw new IllegalArgumentException("Command name cannot be null");
        if(command.getDescription() == null) throw new IllegalArgumentException("Command description cannot be null");
        commands.put(command.getName(), command);
    }

    /**
     * Executes a command based on the event
     *
     * @param event The event to execute
     * @return Whether the command was executed successfully
     */
    public boolean execute(SlashCommandInteractionEvent event) {
        try {
            commands.get(event.getName()).execute(event);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
