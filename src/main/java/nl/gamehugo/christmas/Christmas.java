package nl.gamehugo.christmas;

import nl.gamehugo.christmas.commands.ThrowBotCommand;
import nl.gamehugo.christmas.managers.ButtonManager;
import nl.gamehugo.christmas.managers.CommandManager;
import nl.gamehugo.christmas.managers.ModalManager;
import nl.gamehugo.christmas.managers.SelectionMenuManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import nl.gamehugo.christmas.managers.CooldownManager;

public class Christmas {

    private static JDA jda;
    private static CommandManager commandManager;
    private static ButtonManager buttonManager;
    private static SelectionMenuManager selectionMenuManager;
    private static ModalManager modalManager;
    private static CooldownManager cooldownManager = new CooldownManager();

    public static void main(String[] args) throws InterruptedException {
        // Check if the token is provided
        if(args.length == 0) {
            System.out.println("Please provide a token in the args\nExample: java -jar bot.jar <token>");
            Thread.sleep(5000);
            return;
        }
        for (int i = 0; i < 50; ++i) System.out.println(" "); // just to clear the console and not leak the token
        System.out.println("Token provided starting bot...");
        // Put the token in the first arg
        // Example: java -jar Dino.jar <token>
        jda = JDABuilder.createDefault(args[0])
                .setMemberCachePolicy(MemberCachePolicy.NONE)
                .build();
        // Wait for the bot to be ready
        jda.awaitReady();

        // Register the event listeners
        jda.addEventListener(new EventListener());

        // Initialize the Managers
        cooldownManager = new CooldownManager();
        commandManager = new CommandManager(jda);
        buttonManager = new ButtonManager();
        selectionMenuManager = new SelectionMenuManager();
        modalManager = new ModalManager();

        commandManager.register(new ThrowBotCommand());

        jda.getPresence().setActivity(Activity.customStatus("Merry Christmas!"));
    }

    public static JDA getJDA() {
        return jda;
    }

    public static CooldownManager getCooldownManager() {
        return cooldownManager;
    }
    public static CommandManager getCommandManager() {
        return commandManager;
    }
    public static ButtonManager getButtonManager() {
        return buttonManager;
    }
    public static SelectionMenuManager getSelectionMenuManager() {
        return selectionMenuManager;
    }
    public static ModalManager getModalManager() {
        return modalManager;
    }
}
