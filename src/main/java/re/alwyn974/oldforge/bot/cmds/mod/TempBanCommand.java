package re.alwyn974.oldforge.bot.cmds.mod;


import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import re.alwyn974.oldforge.bot.cmds.utils.Command;

/**
 * TempBan Command
 * 
 * @author <a href="https://github.com/alwyn974"> Alwyn974</a>
 * @version 1.0.4
 * @since 1.0.0
 */
public class TempBanCommand extends Command {

	@Override
	public String getName() {
		return "tempban";
	}

	@Override
	public String getDescription() {
		return "Permet de bannir temporairement.";
	}

	@Override
	public String getUsage() {
		return String.format("%s%s `@member <1m|1h|1d|1M|1Y> [reason]`", getPrefix(), getName());
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
		//TODO
		String userId = !msg.getMentionedMembers().isEmpty() ? msg.getMentionedMembers().get(0).getUser().getId() : args[0];
		int delDays = 1;
		String reason = "Vous n'avez pas respecté le règlement !";
		channel.getGuild().ban(userId, delDays, reason);
	}

}
