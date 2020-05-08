package re.alwyn974.oldforge.bot.cmds.mod;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import re.alwyn974.oldforge.bot.cmds.utils.Command;

/**
 * Unban Command
 * 
 * @author <a href="https://github.com/alwyn974"> Alwyn974</a>
 * @version 1.0.4
 * @since 1.0.0
 */
public class UnbanCommand extends Command {

	@Override
	public String getName() {
		return "unban";
	}

	@Override
	public String getDescription() {
		return "Permet d'unban une personne.";
	}

	@Override
	public String getUsage() {
		return String.format("%s%s `@member`", getPrefix(), getName());
	}

	@Override
	public Permission getPermission() {
		return Permission.BAN_MEMBERS;
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
			try {
				channel.getGuild().unban(userId).queue(success -> {
					channel.sendMessage(success()
							.setDescription(String.format("Successfully unban <@%s>.", userId))
							.build()).queue();
				}, failure -> {
					channel.sendMessage(error()
							.setDescription(String.format("Can't unban <@%s>.\n➳ Erreur : `%s`", userId, failure.getMessage()))
							.build()).queue();
				});
			} catch (Exception e) {
				channel.sendMessage(error().setDescription(String.format("➳ Erreur : `%s`", e.getMessage())).build()).queue();
			}
		}
	}

}
