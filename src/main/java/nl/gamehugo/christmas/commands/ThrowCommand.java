package nl.gamehugo.christmas.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import nl.gamehugo.christmas.Christmas;
import nl.gamehugo.christmas.managers.Command;

public class ThrowCommand implements Command {

    public ThrowCommand() {
        Christmas.getJDA().upsertCommand("throw", "Throw a something at someone!")
                .addOption(OptionType.USER, "user", "The user you want to throw at")
                .addOptions().queue();
    }

    @Override
    public String getName() {
        return "throw";
    }

    @Override
    public String getDescription() {
        return "Throw a something at someone!";
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {

    }
}
