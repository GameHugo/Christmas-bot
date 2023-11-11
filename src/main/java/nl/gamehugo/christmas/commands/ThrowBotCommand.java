package nl.gamehugo.christmas.commands;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import nl.gamehugo.christmas.Christmas;
import nl.gamehugo.christmas.managers.BotCommand;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;

public class ThrowBotCommand implements BotCommand {
    private final Collection<Command.Choice> choices = new ArrayList<>();

    public ThrowBotCommand() {
        choices.add(new Command.Choice("❄️Snowball", "snowball"));
        Christmas.getJDA().upsertCommand("throw", "Throw a something at someone!")
                .addOption(OptionType.USER, "user", "The user you want to throw at", true)
                .addOptions(
                        new OptionData(OptionType.STRING, "item", "Item that you want to throw", true)
                                .addChoices(choices)
                ).queue();
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
        if(isOnCooldown(event.getUser())) {
            event.reply("You can use this command again in " + getCooldown(event.getUser())/1000 + " seconds!").setEphemeral(true).queue();
            return;
        }
        if(event.getOption("item") == null || event.getOption("user") == null) {
            event.reply("You need to specify a user and an item!").queue();
            return;
        }
        String item = Objects.requireNonNull(event.getOption("item")).getAsString();
        User user = Objects.requireNonNull(event.getOption("user")).getAsUser();
        if(containsChoice(item)) return;
        event.reply("You threw a " + item + " at " + user.getAsMention()).queue();
        setCooldown(event.getUser(), 20*1000);
    }

    public boolean containsChoice(String choice) {
        for (Command.Choice choices : choices) {
            if (choices.getName().equals(choice)) {
                return true;
            }
        }
        return false;
    }
}
