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
import java.util.Objects;

public class ThrowCommand implements BotCommand {
    private final Collection<Command.Choice> choices = new ArrayList<>();

    public ThrowCommand() {
        choices.add(new Command.Choice("❄️Snowball", "snowball"));
        choices.add(new Command.Choice("🎁Present", "present"));
        choices.add(new Command.Choice("🍪Cookie", "cookie"));
        choices.add(new Command.Choice("🎄Christmas Tree", "christmas_tree"));
        choices.add(new Command.Choice("🎅Santa", "santa"));
        Christmas.getJDA().upsertCommand(getName(), getDescription())
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
            event.reply("You need to specify a user and an item!").setEphemeral(true).queue();
            return;
        }
        event.deferReply().queue();
        String item = getChoice(Objects.requireNonNull(event.getOption("item")).getAsString());
        User user = Objects.requireNonNull(event.getOption("user")).getAsUser();
        if(item == null) return;
        event.getHook().setEphemeral(false).sendMessage("You threw a **" + item + "** at " + user.getAsMention()).queue();
        setCooldown(event.getUser(), 20*1000);
    }

    public String getChoice(String choice) {
        for (Command.Choice choices : choices) {
            if (choices.getAsString().equals(choice)) {
                return choices.getName();
            }
        }
        return null;
    }
}
