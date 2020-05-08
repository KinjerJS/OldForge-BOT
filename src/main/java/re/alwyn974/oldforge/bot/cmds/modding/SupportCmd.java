package re.alwyn974.oldforge.bot.cmds.modding;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import re.alwyn974.oldforge.bot.cmds.utils.Command;

/**
 * Support Command
 * 
 * @author <a href="https://github.com/alwyn974"> Alwyn974</a>
 * @version 1.0.1
 * @since 1.0.0
 */
public class SupportCmd extends Command {

	@Override
	public String getName() {
		return "support";
	}

	@Override
	public String getDescription() {
		return "Cette commande attribue le rôle Support";
	}

	@Override
	public String getUsage() {
		return String.format("%s%s `<on/off>`", getPrefix(), getName());
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
		if (args.length == 0)
			channel.sendMessage(error().setDescription("Utilisation : " + getUsage()).build()).queue();
		else {
			Role role = channel.getGuild().getRoleById("663017551843950611");
			if (args[0].equals("on"))
				try {
				channel.getGuild().addRoleToMember(msg.getMember(), role).queue(success -> {
					channel.sendMessage(success()
							.setDescription(String.format("Successfully add role `Support` to %s.", sender.getAsMention()))
							.build()).queue();
				});
				} catch (Exception failure) {
					channel.sendMessage(error()
							.setDescription(String.format("Can't add role `Support` to %s.\n➳ Erreur : `%s`", sender.getAsMention(), failure.getMessage()))
							.build()).queue();
				}
			else if (args[0].equals("off"))
				try {
					channel.getGuild().removeRoleFromMember(msg.getMember(), role).queue(success -> {
						channel.sendMessage(success()
								.setDescription(String.format("Successfully remove role `Support` from %s.", sender.getAsMention()))
								.build()).queue();
					});
				} catch (Exception failure) {
					channel.sendMessage(error()
							.setDescription(String.format("Can't remove role `Support` from %s.\n➳ Erreur : `%s`", sender.getAsMention(), failure.getMessage()))
							.build()).queue();
				}
			else {
				channel.sendMessage(error().setDescription("Utilisation : " + getUsage()).build()).queue();
			}
		}
	}

}
