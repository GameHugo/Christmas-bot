package eu.skyrex.countdown;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

public class Countdown {

    private static JDA jda;
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
        //jda.awaitReady();

        jda.getPresence().setActivity(Activity.watching("the countdown"));
    }

    public static JDA getJDA() {
        return jda;
    }
}
