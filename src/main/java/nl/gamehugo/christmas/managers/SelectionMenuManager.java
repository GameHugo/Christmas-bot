package nl.gamehugo.christmas.managers;

import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;

import java.util.HashMap;
import java.util.Map;

public class SelectionMenuManager {
    private final Map<String, BotSelectionMenu> selectionMenus;

    public SelectionMenuManager() {
        selectionMenus = new HashMap<>();
    }

    public void register(String name, BotSelectionMenu botSelectionMenu) {
        selectionMenus.put(name, botSelectionMenu);
    }

    public boolean execute(StringSelectInteractionEvent event) {
        try {
            selectionMenus.get(event.getComponentId()).execute(event);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
