package fr.kinjer.oldforge.bot.command;

import fr.kinjer.oldforge.bot.OldForgeBOT;
import fr.kinjer.oldforge.bot.utils.ScannerPackage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class CommandManager {

    private final List<Command> commands = new ArrayList<>();

    public CommandManager() {
        ScannerPackage.getClassOfPackage(Command.class, "fr.kinjer.oldforge.bot.command.model").forEach(listener -> {
            try {
                this.registerCommand(listener.getConstructor().newInstance());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void registerCommand(Command command) {
        commands.add(command);
    }

    public Command getCommand(String name) {
        return commands.stream().filter(cmd -> cmd.getData().getName().equals(name)).findFirst().orElse(null);
    }

    public List<Command> getCommands() {
        return commands;
    }

    public static class Event extends ListenerAdapter {

        @Override
        public void onReady(@NotNull ReadyEvent event) {
            OldForgeBOT.getInstance().getCommandManager().getCommands().forEach(cmd -> {
                event.getJDA().upsertCommand(cmd.getData()).queue();
            });
        }

        @Override
        public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
            CommandManager commandManager = OldForgeBOT.getInstance().getCommandManager();
            Command command = commandManager.getCommand(event.getName());
            try {
                if (command != null) {
                    if(event.getMember().hasPermission(command.getPermission())) {
                        command.execute(event);
                    } else {
                        event.replyEmbeds(new EmbedBuilder().setColor(Color.red).setTimestamp(OffsetDateTime.now()).setFooter(OldForgeBOT.getBotName(), "https://dl.alwyn974.re/modding_forge_old.png").setDescription("➳ Vous n'avez pas la permission pour utiliser cette commande.").build()).queue();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                event.reply("➳ Une erreur est survenue.").queue();
            }
        }

        @Override
        public void onMessageReceived(@NotNull MessageReceivedEvent event) {
            String msg = event.getMessage().getContentRaw();
            if(msg.startsWith("!")) {
                event.getChannel().sendMessage("Les commandes avec le préfix `" + OldForgeBOT.getPrefix() + "` sont désormais des slash commandes.\n" +
                        "Essayez `/" + msg.substring(1) + "`").queue();
            }
        }
    }

}
