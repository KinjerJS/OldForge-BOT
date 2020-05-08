package re.alwyn974.oldforge.bot.cmds.mod;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import re.alwyn974.oldforge.bot.cmds.utils.Command;

/**
 * Ban Command
 * 
 * @author <a href="https://github.com/alwyn974"> Alwyn974</a>
 * @version 1.0.5
 * @since 1.0.0
 */
public class BanCommand extends Command {

	@Override
	public String getName() {
		return "ban";
	}

	@Override
	public String getDescription() {
		return "Permet de bannir définitivement.";
	}

	@Override
	public String getUsage() {
		return String.format("%s%s `@member <deleteMessageTime> [reason]`", getPrefix(), getName());
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
			StringBuilder builder = new StringBuilder();
			int size = 0;
			for (String arg : args) {
				builder.append(arg.equals(args[0]) ? "" : args.length >= 2 && arg.matches("[0-9]") ? "" : arg);
				size++;
				builder.append(size == 1 || arg.matches("[0-9]") || size == args.length ? "" : " ");
			}
			String reason = args.length == 1 || args.length == 2 && args[1].matches("[0-9]") ? "Vous n'avez pas respecté le règlement !" : builder.toString();		
			try {		
				int delDays = args.length >= 2 && args[1].matches("[0-9]") ? (Integer.valueOf(args[1]) >= 7 ? 7 : Integer.valueOf(args[1])) : 0;
				channel.getGuild().ban(userId, delDays, reason).queue(success -> {
					channel.sendMessage(success()
							.setDescription(String.format("Successfully ban <@%s> for : `%s`", userId, reason))
							.build()).queue();
				});
			} catch (Exception failure) {
				channel.sendMessage(error()
						.setDescription(String.format("Can't ban <@%s>.\n➳ Erreur : `%s`", userId, failure.getMessage()))
						.build()).queue();
			}
		}
	}

}
