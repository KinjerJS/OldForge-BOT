package fr.kinjer.oldforge.bot.command.model.mod;

import fr.kinjer.oldforge.bot.command.Command;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;

public class UnbanCommand implements Command {

	@Override
	public void execute(SlashCommandInteractionEvent event) {
		Member user = event.getOption("user").getAsMember();
		try {
			event.getGuild().unban(user).queue(success -> {
				event.replyEmbeds(success()
						.setDescription(String.format("Successfully unban <@%s>.", user))
						.build()).queue();
			}, failure -> {
				event.replyEmbeds(error()
						.setDescription(String.format("Can't unban <@%s>.\n➳ Erreur : `%s`", user, failure.getMessage()))
						.build()).queue();
			});
		} catch (Exception e) {
			event.replyEmbeds(error().setDescription(String.format("➳ Erreur : `%s`", e.getMessage())).build()).queue();
		}
	}

	@Override
	public SlashCommandData getData() {
		return new CommandDataImpl("unban", "Permet d'unban une personne.")
				.addOption(OptionType.USER, "user", "Utilisateur à unban", true);
	}

	@Override
	public Permission getPermission() {
		return Permission.BAN_MEMBERS;
	}

}
