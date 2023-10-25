package eu.skyrex.countdown.managers;

import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;

import java.util.HashMap;
import java.util.Map;

public class SelectionMenuManager {
    private final Map<String, SelectionMenu> selectionMenus;

    public SelectionMenuManager() {
        selectionMenus = new HashMap<>();
    }

    public void register(String name, SelectionMenu selectionMenu) {
        selectionMenus.put(name, selectionMenu);
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
