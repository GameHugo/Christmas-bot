package nl.gamehugo.christmas.managers;

import net.dv8tion.jda.api.entities.User;
import nl.gamehugo.christmas.Christmas;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public interface BotCommand {
    String getName();
    String getDescription();

    void execute(SlashCommandInteractionEvent event);

    default void upsertCommand() {
        Christmas.getJDA().upsertCommand(getName(), getDescription()).queue();
    }
    default void setCooldown(User user, long cooldown) {
        Christmas.getCooldownManager().setCooldown(user, getName(), cooldown);
    }
    default boolean isOnCooldown(User user) {
        return Christmas.getCooldownManager().isOnCooldown(user, getName());
    }
    default long getCooldown(User user) {
        return Christmas.getCooldownManager().getCooldown(user, getName());
    }
}
