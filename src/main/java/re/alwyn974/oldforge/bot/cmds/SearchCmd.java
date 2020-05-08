package re.alwyn974.oldforge.bot.cmds;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import re.alwyn974.oldforge.bot.cmds.utils.Command;

/**
 * Search Command
 * 
 * @author <a href="https://github.com/alwyn974"> Alwyn974</a>
 * @version 1.0.1
 * @since 1.0.1
 */
public class SearchCmd extends Command {

	@Override
	public String getName() {
		return "search";
	}

	@Override
	public String getDescription() {
		return "Permet d'effectuer une recherche.";
	}

	@Override
	public String getUsage() {
		return String.format("%s%s `<arguments>`", getPrefix(), getName());
	}

	@Override
	public Permission getPermission() {
		return Permission.MESSAGE_WRITE;
	}

	@Override
	public String[] getAliases() {
		return new String[] {"lmgfty"};
	}

	@Override
	public void execute(TextChannel channel, User sender, String[] args, Message msg) {
		StringBuilder builder = new StringBuilder();
		int size = 0;
		for (String text : args) {
			builder.append(text);
			size++;
			builder.append(size != args.length ? "+" : "");
		}
		channel.sendMessage(String.format("https://lmgtfy.com/?q=%s", builder.toString())).queue();
	}

}
