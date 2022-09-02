package fr.kinjer.oldforge.bot.command.model;

import fr.kinjer.oldforge.bot.OldForgeBOT;
import fr.kinjer.oldforge.bot.command.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;

public class HelpCmd implements Command {

	@Override
	public void execute(SlashCommandInteractionEvent event) {
		EmbedBuilder embed = embed();
		embed.setTitle("Voici les commandes disponibles");
		OldForgeBOT.getInstance().getCommandManager().getCommands().forEach(cmd -> {
			embed.addField(cmd.getData().getName(), cmd.getData().getDescription(), false);
		});
		event.replyEmbeds(embed.build()).queue();
	}

	@Override
	public SlashCommandData getData() {
		return new CommandDataImpl("help", "Affiche la liste des commandes disponibles");
	}

	@Override
	public Permission getPermission() {
		return null;
	}

}
