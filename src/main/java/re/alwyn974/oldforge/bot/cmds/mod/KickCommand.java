package re.alwyn974.oldforge.bot.cmds.mod;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import re.alwyn974.oldforge.bot.cmds.utils.Command;

/**
 * Kick Command
 * 
 * @author <a href="https://github.com/alwyn974"> Alwyn974</a>
 * @version 1.0.3
 * @since 1.0.0
 */
public class KickCommand extends Command {

	@Override
	public String getName() {
		return "kick";
	}

	@Override
	public String getDescription() {
		return "Permet d'exclure un membre";
	}

	@Override
	public String getUsage() {
		return String.format("%s%s `@member [reason]`", getPrefix(), getName());
	}

	@Override
	public Permission getPermission() {
		return Permission.KICK_MEMBERS;
	}

	@Override
	public String[] getAliases() {
		return new String[0];
	}

	@Override
	public void execute(TextChannel channel, User sender, String[] args, Message msg) {
		if (args.length < 1)
			channel.sendMessage(error().setDescription("Utilisation : " + getUsage()).build()).queue();
		else {
			String userId = !msg.getMentionedMembers().isEmpty() ? msg.getMentionedMembers().get(0).getUser().getId() : args[0];
			StringBuilder builder = new StringBuilder();
			int size = 0;
			for (String text : args) {
				builder.append(text);
				size++;
				builder.append(size != args.length ? " " : "");
			}
			String reason = args.length == 1 ? "Vous n'avez pas respecté le règlement !" : builder.toString();
			try {
				channel.getGuild().kick(userId, reason).queue(success -> {
					channel.sendMessage(success()
							.setDescription(String.format("Successfully kick <@%s> for : `%s`.", userId, reason))
							.build()).queue();
				});
			} catch (Exception failure) {
				channel.sendMessage(error()
						.setDescription(
								String.format("Can't kick <@%s>.\n➳ Erreur : `%s`", userId, failure.getMessage()))
						.build()).queue();
			}
		}
	}

}
