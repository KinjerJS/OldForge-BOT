package fr.kinjer.oldforge.bot.command.model.modding;

import fr.kinjer.oldforge.bot.command.Command;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;

public class SupportCmd implements Command {

	@Override
	public void execute(SlashCommandInteractionEvent event) {
		Member member = event.getMember();
		Guild guild = event.getGuild();
		if(member == null || guild == null) return;

		Role role = guild.getRoleById("663017551843950611");
		boolean activate = event.getOption("activer").getAsBoolean();

		if(role == null) return;

		if (activate) {
			try {
				guild.addRoleToMember(member, role).queue(success -> {
					event.replyEmbeds(success()
							.setDescription(String.format("Successfully add role `Support` to %s.", member.getAsMention()))
							.build()).queue();
				});
			} catch (Exception failure) {
				event.replyEmbeds(error()
						.setDescription(String.format("Can't add role `Support` to %s.\n➳ Erreur : `%s`", member.getAsMention(), failure.getMessage()))
						.build()).queue();
			}
			return;
		}
		try {
			guild.removeRoleFromMember(member, role).queue(success -> {
				event.replyEmbeds(success()
						.setDescription(String.format("Successfully remove role `Support` from %s.", member.getAsMention()))
						.build()).queue();
			});
		} catch (Exception failure) {
			event.replyEmbeds(error()
					.setDescription(String.format("Can't remove role `Support` from %s.\n➳ Erreur : `%s`", member.getAsMention(), failure.getMessage()))
					.build()).queue();
		}
	}

	@Override
	public SlashCommandData getData() {
		return new CommandDataImpl("support", "Cette commande attribue le rôle Support")
				.addOption(OptionType.BOOLEAN, "activer", "Active le rôle Support", true);
	}

	@Override
	public Permission getPermission() {
		return null;
	}

}
