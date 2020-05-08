package re.alwyn974.oldforge.bot.cmds;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import re.alwyn974.oldforge.bot.cmds.utils.Command;

/**
 * Haste Command
 * 
 * @author <a href="https://github.com/alwyn974"> Alwyn974</a>
 * @version 1.0.0
 * @since 1.0.0
 */
public class HasteCmd extends Command {

	@Override
	public String getName() {
		return "haste";
	}

	@Override
	public String getDescription() {
		return "Envoie un lien vers hastebin";
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
		return new String[] {"hastebin"};
	}

	@Override
	public void execute(TextChannel channel, User sender, String[] args, Message msg) {
		channel.sendMessage("https://code.alwyn974.re").queue();
	}

}
