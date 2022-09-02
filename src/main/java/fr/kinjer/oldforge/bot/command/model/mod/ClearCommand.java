package fr.kinjer.oldforge.bot.command.model.mod;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import fr.kinjer.oldforge.bot.command.Command;
import fr.kinjer.oldforge.bot.utils.ThreadUtil;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;

public class ClearCommand implements Command {

	@Override
	public void execute(SlashCommandInteractionEvent event) {
		Guild guild = event.getGuild();
		OptionMapping optionUser = event.getOption("user");

		int number = event.getOption("number").getAsInt();
		Member member = optionUser != null ? optionUser.getAsMember() : null;
		MessageChannel channel = event.getMessageChannel();

		InteractionHook msg = event.reply("Attend...").complete();

		int messagesToDelete = this.deleteMessages(number, member, channel, msg.retrieveOriginal().complete());

		msg.editOriginal(this.getMessage(member, messagesToDelete, guild))
				.queue(message -> ThreadUtil.taskLater(3000, () -> message.delete().queue()));
	}

	private String getMessage(Member member, int messagesToDelete, Guild guild) {
		if(member != null) {
			return messagesToDelete + " messages supprimé de " + member.getAsMention() + "!";
		} else {
			return messagesToDelete + " messages supprimé!";
		}
	}

	private int deleteMessages(int number, Member member, MessageChannel channel, Message... ignoredMessages) {
		number++;
		if(number < 1) {
			number = 1;
		}
		int numberToDelete = Math.min(number, 100);
		List<Message> messagesToDelete = channel.getHistory().retrievePast(numberToDelete).complete()
				.stream().filter(message -> !Arrays.asList(ignoredMessages).contains(message)).collect(Collectors.toList());
		if(member != null) {
			messagesToDelete = messagesToDelete.stream()
					.filter(message -> message.getMember().getIdLong() == member.getIdLong())
					.collect(Collectors.toList());
		}

		channel.purgeMessages(messagesToDelete);

		if(messagesToDelete.size() > number) {
			return deleteMessages(number - 100, member, channel, ignoredMessages);
		}

		return messagesToDelete.size();
	}

	@Override
	public SlashCommandData getData() {
		return new CommandDataImpl("clear", "Permet de supprimer des messages")
				.addOption(OptionType.INTEGER, "number", "Nombre de messages à supprimer", true)
				.addOption(OptionType.USER, "member", "Utilisateur à supprimer");
	}

	@Override
	public Permission getPermission() {
		return Permission.MESSAGE_MANAGE;
	}

}
