package re.alwyn974.oldforge.bot.cmds;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import re.alwyn974.oldforge.bot.cmds.utils.Command;
import re.alwyn974.oldforge.bot.cmds.utils.CommandManager;

/**
 * Help Command
 * 
 * @author <a href="https://github.com/alwyn974"> Alwyn974</a>
 * @version 1.0.0
 * @since 1.0.0
 */
public class HelpCmd extends Command {

	@Override
	public String getName() {
		return "help";
	}

	@Override
	public String getDescription() {
		return "Affiche la liste des commandes disponibles";
	}

	@Override
	public String getUsage() {
		return String.format("%s%s", getPrefix(), getName());
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
		EmbedBuilder embed = embed();
		embed.setTitle("Voici les commandes disponibles");
		CommandManager.getCommandsList().forEach(cmd -> {
			if (msg.getMember().hasPermission(cmd.getPermission())) 
				embed.addField(cmd.getName(), String.format("%s\n➳ La syntaxe est : `%s`", cmd.getDescription(), cmd.getUsage().replace("`", "")), false);
		});	
		channel.sendMessage(embed.build()).queue();
	}

}
