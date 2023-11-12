package nl.gamehugo.christmas.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import nl.gamehugo.christmas.Christmas;
import nl.gamehugo.christmas.managers.BotButton;
import nl.gamehugo.christmas.managers.BotCommand;
import nl.gamehugo.christmas.database.Database;
import nl.gamehugo.christmas.managers.BotModal;
import nl.gamehugo.christmas.utils.Tree;

import java.util.Date;
import java.util.Objects;

public class TreeCommand implements BotCommand, BotButton, BotModal {

    public TreeCommand() {
        Christmas.getJDA().upsertCommand(getName(), getDescription())
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR)
                ).queue();
        registerButtons("water", "rename");
        registerModal("rename-tree");
    }

    @Override
    public String getName() {
        return "tree";
    }

    @Override
    public String getDescription() {
        return "Manage your guild tree";
    }

    private final Database database = Database.getInstance();

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        if(event.getGuild() == null) {
            event.reply("You can only use this command in a guild!").queue();
            return;
        }
        event.deferReply().queue();
        if(!database.getTreeDAO().treeExistsByGuild(event.getGuild().getIdLong())) {
            Tree tree = new Tree(event.getGuild().getIdLong(), "My Tree", 0, new Date().getTime());
            if(database.getTreeDAO().insertTree(tree)) {
                event.getHook().sendMessage("You didn't have a tree yet so I created one for you!").queue();
            } else {
                event.getHook().sendMessage("Something went wrong while creating your tree!").queue();
                return;
            }
        }
        Tree tree = database.getTreeDAO().getTreeByGuild(event.getGuild().getIdLong());
        event.getHook().sendMessageEmbeds(getEmbed(event.getGuild(), tree)).addActionRow(
                Button.primary("water", "💧Water Tree"),
                Button.primary("rename", "✏️Rename Tree")
        ).queue();
    }

    @Override
    public void execute(ButtonInteractionEvent event) {
        if(event.getGuild() == null) {
            event.reply("You can only use this command in a guild!").queue();
            return;
        }
        Tree tree = database.getTreeDAO().getTreeByGuild(event.getGuild().getIdLong());
        if(event.getComponentId().equals("water")) {
            event.deferEdit().queue();
            if(tree.getLastWatered() + 10*1000 > new Date().getTime()) {
                event.getHook().sendMessage("You can water your tree in "+(10-(new Date().getTime()-tree.getLastWatered())/1000)+" seconds!").queue();
                return;
            }
            tree.setSize(tree.getSize()+1);
            tree.setLastWatered(new Date().getTime());
            database.getTreeDAO().updateTree(tree);
            event.getHook().sendMessage("You watered your tree!").queue();
            event.getHook().editOriginalEmbeds(getEmbed(event.getGuild(), tree)).queue();
        } else if(event.getComponentId().equals("rename")) {
            TextInput textInput = TextInput.create("rename", "Rename your tree", TextInputStyle.SHORT)
                    .setPlaceholder("Tree name")
                    .setMinLength(3)
                    .setMaxLength(50)
                    .build();
            Modal modal = Modal.create("rename-tree", "Rename your tree")
                    .addComponents(ActionRow.of(textInput))
                    .build();
            event.replyModal(modal).queue();
        }
    }

    public MessageEmbed getEmbed(Guild guild, Tree tree) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(guild.getName()+"'s Tree");
        embedBuilder.setDescription("This is your guild tree!");
        embedBuilder.addField("Name", tree.getName(), true);
        embedBuilder.addField("Size", tree.getSize()+"", true);
        embedBuilder.setColor(0x00ff00);
        return embedBuilder.build();
    }

    @Override
    public void execute(ModalInteractionEvent event) {
        if(event.getGuild() == null) {
            event.reply("You can only use this command in a guild!").queue();
            return;
        }
        Tree tree = database.getTreeDAO().getTreeByGuild(event.getGuild().getIdLong());
        if(event.getModalId().equals("rename-tree")) {
            event.deferReply().queue();
            if (event.getValue("rename") == null) {
                event.getHook().sendMessage("You didn't enter a name!").queue();
                return;
            }
            String name = Objects.requireNonNull(event.getValue("rename")).getAsString();
            tree.setName(name);
            database.getTreeDAO().updateTree(tree);
            event.getHook().sendMessage("You renamed your tree to "+name+"!")
                    .addEmbeds(getEmbed(event.getGuild(), tree))
                    .addActionRow(
                            Button.primary("water", "💧Water Tree"),
                            Button.primary("rename", "✏️Rename Tree")
                    ).queue();
        }
    }
}
