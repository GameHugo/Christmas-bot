package nl.gamehugo.christmas.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import net.dv8tion.jda.api.utils.FileUpload;
import net.dv8tion.jda.internal.utils.PermissionUtil;
import nl.gamehugo.christmas.Christmas;
import nl.gamehugo.christmas.managers.BotButton;
import nl.gamehugo.christmas.managers.BotCommand;
import nl.gamehugo.christmas.database.Database;
import nl.gamehugo.christmas.managers.BotModal;
import nl.gamehugo.christmas.utils.Tree;

import java.io.File;
import java.net.URISyntaxException;
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
        if(!PermissionUtil.checkPermission((TextChannel)event.getChannel(), event.getGuild().getSelfMember(), Permission.VIEW_CHANNEL)) {
            event.reply("I don't have permission to view this channel!").queue();
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
        sendTree(tree, event.getHook());
    }

    @Override
    public void execute(ButtonInteractionEvent event) {
        if(event.getGuild() == null) {
            event.reply("You can only use this command in a guild!").queue();
            return;
        }
        Tree tree = database.getTreeDAO().getTreeByGuild(event.getGuild().getIdLong());
        if(event.getComponentId().equals("water")) {
            // Water button interaction
            event.deferEdit().queue();
            waterTree(tree, event.getHook());
        } else if(event.getComponentId().equals("rename")) {
            // Rename button interaction
            event.replyModal(getRenameModal()).queue();
        }
    }

    @Override
    public void execute(ModalInteractionEvent event) {
        if(event.getGuild() == null) {
            event.reply("You can only use this command in a guild!").queue();
            return;
        }
        Tree tree = database.getTreeDAO().getTreeByGuild(event.getGuild().getIdLong());
        if(event.getModalId().equals("rename-tree")) {
            // Rename modal interaction
            event.deferReply().queue();
            if (event.getValue("rename") == null) {
                event.getHook().sendMessage("You didn't enter a name!").queue();
                return;
            }
            String name = Objects.requireNonNull(event.getValue("rename")).getAsString();
            renameTree(tree, event.getHook(), name);
        }
    }

    private MessageEmbed getEmbed(Guild guild, Tree tree) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(guild.getName()+"'s Tree");
        embedBuilder.setDescription("This is your guild tree!");
        embedBuilder.setImage("attachment://tree.png");
        embedBuilder.addField("Name", tree.getName(), true);
        embedBuilder.addField("Size", tree.getSize()+"", true);
        embedBuilder.setColor(0x00ff00);
        return embedBuilder.build();
    }

    private Modal getRenameModal() {
        TextInput textInput = TextInput.create("rename", "Rename your tree", TextInputStyle.SHORT)
                .setPlaceholder("Tree name")
                .setMinLength(3)
                .setMaxLength(50)
                .build();
        return Modal.create("rename-tree", "Rename your tree")
                .addComponents(ActionRow.of(textInput))
                .build();
    }

    private void waterTree(Tree tree, InteractionHook hook) {
        if(!canWaterTree(tree)) {
            int minutes = (int) ((tree.getLastWatered() + (long) waterDelayInMinutes *60*1000 - new Date().getTime())/1000/60);
            if(minutes > 0) {
                hook.sendMessage("You can water the tree again in "+(minutes+1)+" minutes!").setEphemeral(true).queue(); // +1 because 0 minutes is 1 minute
            } else {
                int seconds = (int) ((tree.getLastWatered() + (long) waterDelayInMinutes *60*1000 - new Date().getTime())/1000);
                hook.sendMessage("You can water the tree again in "+seconds+" seconds!").setEphemeral(true).queue();
            }
            return;
        }
        tree.setSize(tree.getSize()+1);
        tree.setLastWatered(new Date().getTime());
        database.getTreeDAO().updateTree(tree);
        hook.sendMessage("You watered your tree!").queue();
        Guild guild = Christmas.getJDA().getGuildById(tree.getGuildID());
        if(guild == null) {
            hook.sendMessage("Something went wrong while getting your guild!").queue();
            return;
        }
        hook.editOriginalEmbeds(getEmbed(guild, tree)).queue();
    }
    int waterDelayInMinutes = 60;
    private boolean canWaterTree(Tree tree) {
        return tree.getLastWatered() + (long) waterDelayInMinutes *60*1000 < new Date().getTime();
    }

    private void renameTree(Tree tree, InteractionHook hook, String name) {
        tree.setName(name);
        database.getTreeDAO().updateTree(tree);
        Guild guild = Christmas.getJDA().getGuildById(tree.getGuildID());
        if(guild == null) {
            hook.sendMessage("Something went wrong while getting your guild!").queue();
            return;
        }
        sendTree(tree, hook);
    }

    private void sendTree(Tree tree, InteractionHook hook) {
        File file;
        try {
            if(tree.getSize() > 10) {
                file = Christmas.getFileFromResource("trees/tree-100.png");
            } else {
                file = Christmas.getFileFromResource("trees/tree-1.png");
            }
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        Guild guild = Christmas.getJDA().getGuildById(tree.getGuildID());
        if(guild == null) {
            hook.sendMessage("Something went wrong while getting your guild!").queue();
            return;
        }
        hook.sendFiles(FileUpload.fromData(file, "tree.png")).setEmbeds(getEmbed(guild, tree)).addActionRow(
                Button.primary("water", "💧Water Tree"),
                Button.primary("rename", "✏️Rename Tree")
        ).queue();
    }
}
