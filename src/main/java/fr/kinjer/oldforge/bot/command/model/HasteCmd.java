package fr.kinjer.oldforge.bot.command.model;

import fr.kinjer.oldforge.bot.command.Command;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;

public class HasteCmd implements Command {

	@Override
	public void execute(SlashCommandInteractionEvent event) {
		event.reply("https://code.alwyn974.re").addActionRow(Button.link("https://code.alwyn974.re", "Hastebin")).queue();
	}

	@Override
	public SlashCommandData getData() {
		return new CommandDataImpl("haste", "Envoie un lien vers hastebin");
	}

	@Override
	public Permission getPermission() {
		return null;
	}

}
