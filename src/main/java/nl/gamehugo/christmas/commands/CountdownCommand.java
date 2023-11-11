package nl.gamehugo.christmas.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import nl.gamehugo.christmas.managers.BotCommand;

import java.util.Calendar;
import java.util.Date;

public class CountdownCommand implements BotCommand {

    public CountdownCommand() {
        upsertCommand();
    }

    @Override
    public String getName() {
        return "countdown";
    }

    @Override
    public String getDescription() {
        return "Countdown to Christmas!";
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        long diff = getDifference();

        long seconds = diff / 1000 % 60;
        long minutes = diff / (1000 * 60) % 60;
        long hours = diff / (1000 * 60 * 60) % 24;
        long days = diff / (1000 * 60 * 60 * 24);

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Countdown to Christmas!");
        embedBuilder.setDescription("Time left until Christmas!");
        embedBuilder.setColor(0x4287f5);
        embedBuilder.addField("Days", String.valueOf(days), true);
        embedBuilder.addField("Hours", String.valueOf(hours), true);
        embedBuilder.addField("Minutes", String.valueOf(minutes), true);
        embedBuilder.addField("Seconds", String.valueOf(seconds), true);
        embedBuilder.setFooter("Merry Christmas!", event.getUser().getAvatarUrl());
        embedBuilder.setTimestamp(new Date().toInstant());

        event.replyEmbeds(embedBuilder.build()).setEphemeral(true).queue();
    }

    private static long getDifference() {
        Calendar now = Calendar.getInstance();
        Calendar christmasCalendar = Calendar.getInstance();
        christmasCalendar.set(Calendar.YEAR, now.get(Calendar.YEAR));
        christmasCalendar.set(Calendar.MONTH, Calendar.DECEMBER);
        christmasCalendar.set(Calendar.DAY_OF_MONTH, 25);
        christmasCalendar.set(Calendar.HOUR_OF_DAY, 0);
        christmasCalendar.set(Calendar.MINUTE, 0);
        christmasCalendar.set(Calendar.SECOND, 0);

        long christmas = christmasCalendar.getTimeInMillis();
        return christmas - now.getTimeInMillis();
    }
}
