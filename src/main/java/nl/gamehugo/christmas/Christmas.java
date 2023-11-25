package nl.gamehugo.christmas;

import nl.gamehugo.christmas.commands.CountdownCommand;
import nl.gamehugo.christmas.commands.ThrowCommand;
import nl.gamehugo.christmas.commands.TreeCommand;
import nl.gamehugo.christmas.managers.ButtonManager;
import nl.gamehugo.christmas.managers.CommandManager;
import nl.gamehugo.christmas.managers.ModalManager;
import nl.gamehugo.christmas.managers.SelectionMenuManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import nl.gamehugo.christmas.managers.CooldownManager;
import nl.gamehugo.christmas.database.Database;
import org.simpleyaml.configuration.file.YamlFile;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

public class Christmas {

    private static JDA jda;
    private static CommandManager commandManager;
    private static ButtonManager buttonManager;
    private static SelectionMenuManager selectionMenuManager;
    private static ModalManager modalManager;
    private static CooldownManager cooldownManager;

    private static YamlFile config;

    public static void main(String[] args) throws InterruptedException, IOException, URISyntaxException {
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

        // Initialize config file
        config = new YamlFile("config.yml");
        if(!config.exists()) {
            // create the file with default values from the resource
            System.out.println("Config file not found, creating one...");
            config.createNewFile();
            config.load(Christmas.class.getClassLoader().getResourceAsStream("config.yml"));
            config.save();
        }
        try {
            config.load();
        } catch (Exception e) {
            System.out.println("Error while loading config file: " + e.getMessage());
        }

        // Initialize the database
        Database database = new Database(
                config.getString("database.ip"),
                config.getInt("database.port"),
                config.getString("database.database"),
                config.getString("database.user"),
                config.getString("database.password")
        );
        try {
            database.connect();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        // Initialize the Managers
        cooldownManager = new CooldownManager();
        commandManager = new CommandManager(jda);
        buttonManager = new ButtonManager();
        selectionMenuManager = new SelectionMenuManager();
        modalManager = new ModalManager();

        // Register the commands
        commandManager.register(new ThrowCommand());
        commandManager.register(new CountdownCommand());

        TreeCommand treeCommand = new TreeCommand();
        commandManager.register(treeCommand);

        jda.getPresence().setActivity(Activity.customStatus("Merry Christmas!"));

        System.out.println("Started!");
    }

    public static File getFileFromResource(String fileName) throws URISyntaxException {
        ClassLoader classLoader = Christmas.class.getClassLoader();
        URL resource = classLoader.getResource(fileName);
        if (resource == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {
            return new File(resource.toURI());
        }
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
