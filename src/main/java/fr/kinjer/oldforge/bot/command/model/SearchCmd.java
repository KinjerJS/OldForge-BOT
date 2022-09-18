package fr.kinjer.oldforge.bot.command.model;

import fr.kinjer.oldforge.bot.command.Command;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;

public class SearchCmd implements Command {

	@Override
	public void execute(SlashCommandInteractionEvent event) {
		OptionMapping option = event.getOption("name");

		if(option != null) {
			event.reply("https://lmgtfy.com/?q=" + option.getAsString().replace(" ", "%20")).queue();
		}
	}

	@Override
	public SlashCommandData getData() {
		return new CommandDataImpl("search", "Permet d'effectuer une recherche.")
				.addOption(OptionType.STRING, "name", "Texte à rechercher", true);
	}

	@Override
	public Permission getPermission() {
		return null;
	}

}
