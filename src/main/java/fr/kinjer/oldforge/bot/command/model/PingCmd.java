package fr.kinjer.oldforge.bot.command.model;

import java.time.OffsetDateTime;

import fr.kinjer.oldforge.bot.command.Command;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;

public class PingCmd implements Command {

	@Override
	public void execute(SlashCommandInteractionEvent event) {
		event.deferReply(false).queue(msg -> {
			msg.editOriginalEmbeds(embed(":ping_pong: Pong: `" + (Math.abs(OffsetDateTime.now().getNano() - msg.getInteraction().getTimeCreated().getNano()) / 1000000) + " ms` ").build()).queue();
		});
	}

	@Override
	public SlashCommandData getData() {
		return new CommandDataImpl("ping", "Donne le temps de latence");
	}

	@Override
	public Permission getPermission() {
		return null;
	}

}
