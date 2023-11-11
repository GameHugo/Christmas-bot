package nl.gamehugo.christmas.managers;

import net.dv8tion.jda.api.entities.User;

import java.util.HashMap;
import java.util.Map;

public class CooldownManager {
    Map<User, Map<String, Long>> cooldowns = new HashMap<>();

    /**
     * Set a cooldown for a user
     * @param user The user to set the cooldown for
     * @param id The id of the cooldown
     * @param cooldown The cooldown in milliseconds
     */
    public void setCooldown(User user, String id, long cooldown) {
        if (!cooldowns.containsKey(user)) {
            cooldowns.put(user, new HashMap<>());
        }
        cooldowns.get(user).put(id, System.currentTimeMillis() + cooldown);
    }

    /**
     * Check if a user is on cooldown
     * @param user The user to check
     * @param id The id of the cooldown
     * @return Whether the user is on cooldown
     */
    public boolean isOnCooldown(User user, String id) {
        if (!cooldowns.containsKey(user)) {
            return false;
        }
        if (!cooldowns.get(user).containsKey(id)) {
            return false;
        }
        if (cooldowns.get(user).get(id) < System.currentTimeMillis()) {
            cooldowns.get(user).remove(id);
            return false;
        }
        return true;
    }

    /**
     * Get the cooldown of a user
     * @param user The user to get the cooldown of
     * @param id The id of the cooldown
     * @return The cooldown in milliseconds
     */
    public long getCooldown(User user, String id) {
        if (!cooldowns.containsKey(user)) {
            return 0;
        }
        if (!cooldowns.get(user).containsKey(id)) {
            return 0;
        }
        return cooldowns.get(user).get(id) - System.currentTimeMillis();
    }
}
