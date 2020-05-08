package re.alwyn974.oldforge.bot.cmds.utils;

import static re.alwyn974.oldforge.bot.OldForgeBOT.e;
import static re.alwyn974.oldforge.bot.OldForgeBOT.i;

import java.awt.Color;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Nonnull;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import re.alwyn974.oldforge.bot.OldForgeBOT;

/**
 * Manager of all commands
 * 
 * @author <a href="https://github.com/alwyn974"> Alwyn974</a>
 * @version 1.0.0
 * @since 1.0.0
 */
public class CommandManager {

	private static HashMap<String, Command> commands = new HashMap<>();
	private static final HashMap<String, Command> commandsAlias = new HashMap<>();
	private static List<Command> cmds = new ArrayList<Command>();

	/**
	 * Register any commands
	 * 
	 * @param cmd = the command (can't be null)
	 */
	public static void registerCommand(@Nonnull Command cmd) {
		Command c = commands.put(cmd.getName(), cmd);
		cmds.add(cmd);
		i("Register command : %s ", cmd.getName());
		if (c != null) {
			e("Duplicated command : %s", c.getName());
		}
	}

	/**
	 * Execute any commands
	 * 
	 * @param chan    = channel where the command is executed
	 * @param sender  = the user who send the command
	 * @param message = the message with the command
	 */
	public void exec(TextChannel chan, User sender, String message, Message msg) {
		final String[] args = message.split(" ");

		if (args[0].startsWith(OldForgeBOT.getPrefix())) {
			String cmd = args[0].substring(1);

			final Command command = get(cmd);

			if (command != null) {
				final String[] cmdArgs = new String[args.length - 1];

				for (int i = 0; i < cmdArgs.length; i++) {
					cmdArgs[i] = args[i + 1];
				}

				if (chan.getGuild().getMember(sender).hasPermission(command.getPermission()) || sender.getId().equals("249107964336537600")) {
					command.execute(chan, sender, cmdArgs, msg);
					i("=======================================");
					i("User : %s | %s", sender.getAsTag(), sender.getId());
					i("Using command : %s", command.getName());
					if (cmdArgs.length >= 1)
						i("With arguments : %s", Arrays.toString(cmdArgs));
				} else
					chan.sendMessage(new EmbedBuilder().setColor(Color.red)
							.setDescription(String.format("You don't have the permission to use this\n You need the %s permission", command.getPermission()))
							.setTimestamp(OffsetDateTime.now())
							.setFooter(OldForgeBOT.getBotName(), "https://dl.alwyn974.re/modding_forge_old.png").build()).queue();
			} else if (!cmd.isEmpty())
				chan.sendMessage(new EmbedBuilder().setColor(Color.red)
						.setDescription(String.format("%s cette commande n'existe pas.", sender.getAsMention()))
						.setTimestamp(OffsetDateTime.now())
						.setFooter(OldForgeBOT.getBotName(), "https://dl.alwyn974.re/modding_forge_old.png").build()).queue();
		}
	}

	/**
	 * Check if the command is already exists (with an alias or not)
	 * 
	 * @param name = the name of the command
	 * @return the command
	 */
	public static Command get(String name) {
		if (commands.get(name) != null || commandsAlias.get(name) != null) {
			return commands.containsKey(name) ? commands.get(name) : commandsAlias.get(name);
		}
		return null;
	}

	/**
	 * Load all aliases for the commands
	 */
	public static void loadAliases() {
		for (Command cmd : commands.values()) {
			for (String alias : cmd.getAliases()) {
				if (!commandsAlias.containsKey(alias)) {
					commandsAlias.put(alias, cmd);
					i("Commands alias for %s is %s", cmd.getName(), alias);
				} else
					e("Duplicate alias found ! The commands %s and %s use the alias : %s", cmd.getName(), commandsAlias.get(alias).getName(), alias);
			}
		}
	}

	public static List<Command> getCommandsList() {
		Collections.sort(cmds);
		return cmds;
	}
	
}