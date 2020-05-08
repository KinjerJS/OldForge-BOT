package re.alwyn974.oldforge.bot.cmds.mod;

import java.util.ArrayList;
import java.util.List;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import re.alwyn974.oldforge.bot.cmds.utils.Command;

/**
 * Clear Command
 * 
 * @author <a href="https://github.com/alwyn974"> Alwyn974</a>
 * @version 1.0.5
 * @since 1.0.5
 */
public class ClearCommand extends Command {

	@Override
	public String getName() {
		return "clear";
	}

	@Override
	public String getDescription() {
		return "Permet de supprimer des messages";
	}

	@Override
	public String getUsage() {
		return String.format("%s%s `[number] <@member>`", getPrefix(), getName());
	}

	@Override
	public Permission getPermission() {
		return Permission.MESSAGE_MANAGE;
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
			MessageHistory history = channel.getHistory();
			String userId = args.length == 2 ? !msg.getMentionedMembers().isEmpty() ? msg.getMentionedMembers().get(0).getId() : args[1] : "";
			try {
				if (args[0].matches("([0-9]*)")) {
					final List<Message> msgs = new ArrayList<Message>();
					long timeStarted = System.currentTimeMillis();
					int number = Integer.parseInt(args[0]) >= 99 ? 99 : Integer.parseInt(args[0]);
					if (args.length == 2) {
						for (Message m : history.retrievePast(number).complete()) {
							if (m.getAuthor().getId().equals(userId))
								msgs.add(m);
						}
					} else
						msgs.addAll(history.retrievePast(number).complete());

					channel.deleteMessages(msgs).queue(sucess -> {
						channel.sendMessage(success()
								.setDescription(String.format("Successfully clear all `%d` message%s in `%dms`" + 
										(args.length == 2 ? " for <@" + userId + ">" : ""), msgs.size(),
										(msgs.size() > 1 ? "s" : ""), (System.currentTimeMillis() - timeStarted)))
								.build()).queue();
						}, failure -> {
							channel.sendMessage(error()
									.setDescription(String.format("Can't clear the message.\n➳ Erreur : `%s`", failure.getMessage()))
									.build()).queue();
							});
					} else
						channel.sendMessage(error().setDescription("Utilisation : " + getUsage()).build()).queue();
				} catch (Exception failure) {
					channel.sendMessage(error()
							.setDescription(String.format("Can't clear the message.\n➳ Erreur : `%s`", failure.getMessage()))
							.build()).queue();
			}
		}
	}

}
