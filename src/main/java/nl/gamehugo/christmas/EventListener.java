package nl.gamehugo.christmas;

import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import org.jetbrains.annotations.NotNull;

public class EventListener implements net.dv8tion.jda.api.hooks.EventListener {

    @Override
    public void onEvent(@NotNull GenericEvent genericEvent) {
        if (genericEvent instanceof SlashCommandInteractionEvent) onSlashCommandInteraction((SlashCommandInteractionEvent) genericEvent);
        if (genericEvent instanceof ButtonInteractionEvent) onButtonInteraction((ButtonInteractionEvent) genericEvent);
        if (genericEvent instanceof StringSelectInteractionEvent) onSelectionMenuInteraction((StringSelectInteractionEvent) genericEvent);
        if (genericEvent instanceof ModalInteractionEvent) onModalInteraction((ModalInteractionEvent) genericEvent);
    }

    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if(!Christmas.getCommandManager().execute(event)) {
            event.getHook().sendMessage("An error occurred while executing the command, Please contact a admin.").queue();
        }
    }
    public void onButtonInteraction(ButtonInteractionEvent event) {
        if(!Christmas.getButtonManager().execute(event)) {
            event.getHook().sendMessage("An error occurred while executing the command, Please contact a admin.").queue();
        }
    }
    public void onSelectionMenuInteraction(StringSelectInteractionEvent event) {
        if(!Christmas.getSelectionMenuManager().execute(event)) {
            event.getHook().sendMessage("An error occurred while executing the command, Please contact a admin.").queue();
        }
    }
    public void onModalInteraction(ModalInteractionEvent event) {
        if(!Christmas.getModalManager().execute(event)) {
            event.getHook().sendMessage("An error occurred while executing the command, Please contact a admin.").queue();
        }
    }
}
