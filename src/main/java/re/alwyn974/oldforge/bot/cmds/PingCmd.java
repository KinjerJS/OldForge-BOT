package re.alwyn974.oldforge.bot.cmds;

import java.time.OffsetDateTime;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import re.alwyn974.oldforge.bot.cmds.utils.Command;

/**
 * Ping Command
 * 
 * @author <a href="https://github.com/alwyn974"> Alwyn974</a>
 * @version 1.0.0
 * @since 1.0.0
 */
public class PingCmd extends Command {

	@Override
	public String getName() {
		return "ping";
	}

	@Override
	public String getDescription() {
		return "Donne le temps de latence";
	}

	@Override
	public String getUsage() {
		return getPrefix() + getName();
	}

	@Override
	public Permission getPermission() {
		return Permission.MESSAGE_WRITE;
	}

	@Override
	public String[] getAliases() {
		return new String[0];
	}

	@Override
	public void execute(TextChannel channel, User sender, String[] args, Message msg) {
		channel.sendMessage(embed(String.format(":ping_pong: Pong: `%s ms` ", Math.abs(OffsetDateTime.now().getNano() - msg.getTimeCreated().getNano()) / 1000000)).build()).queue();
	}

}
