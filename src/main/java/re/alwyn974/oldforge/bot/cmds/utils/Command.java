package re.alwyn974.oldforge.bot.cmds.utils;

import java.awt.Color;
import java.time.OffsetDateTime;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed.Field;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import re.alwyn974.oldforge.bot.OldForgeBOT;

/**
 * Useful class for creating a command
 * 
 * @author <a href="https://github.com/alwyn974"> Alwyn974</a>
 * @version 1.0.0
 * @since 1.0.0
 */
public abstract class Command implements Comparable<Command>{

	/**
	 * ID of ! αℓωүη974#0001
	 */
	protected final String id_alwyn = "249107964336537600";
	
	/**
	 * Get the prefix for all commands
	 * 
	 * @return the prefix of the command
	 */
	public String getPrefix() {
		return OldForgeBOT.getPrefix();
	}

	/**
	 * What should be typed to trigger this command (Without prefix)
	 * 
	 * @return name
	 */
	public abstract String getName();

	/**
	 * A short description of the method
	 * 
	 * @return description
	 */
	public abstract String getDescription();

	/**
	 * How to use the command ?
	 * 
	 * @return String of usage
	 */
	public abstract String getUsage();

	/**
	 * Permission to use the command
	 * 
	 * @return permission
	 */
	public abstract Permission getPermission();

	/**
	 * Alias to call the command
	 * 
	 * @return array of aliases
	 */
	public abstract String[] getAliases();

	/**
	 * @param channel = channel where the command is executed
	 * @param sender  = the user who send the command
	 * @param args    = the arguments after the command
	 */
	public abstract void execute(TextChannel channel, User sender, String[] args, Message msg);
	
	
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Command cmd) {
		return this.getName().compareTo(cmd.getName());
	}

	/**
	 * EmbedBuilder with green color, for task successful
	 * 
	 * @return EmbedBuilder
	 */
	public EmbedBuilder success() {
		return new EmbedBuilder().setColor(Color.green).setTimestamp(OffsetDateTime.now()).setFooter(OldForgeBOT.getBotName(), "https://dl.alwyn974.re/modding_forge_old.png");
	}

	/**
	 * EmbedBuilder with red color, for task not successful
	 * 
	 * @return EmbedBuilder
	 */
	public EmbedBuilder error() {
		return new EmbedBuilder().setColor(Color.red).setTimestamp(OffsetDateTime.now()).setFooter(OldForgeBOT.getBotName(), "https://dl.alwyn974.re/modding_forge_old.png");
	}

	/**
	 * EmbedBuild with cyan color, for any Embed I have to use
	 * 
	 * @return EmbedBuilder
	 */
	public EmbedBuilder embed() {
		return new EmbedBuilder().setColor(Color.cyan).setTimestamp(OffsetDateTime.now()).setThumbnail("https://dl.alwyn974.re/modding_forge_old.png").setFooter(OldForgeBOT.getBotName(), "https://dl.alwyn974.re/modding_forge_old.png");
	}

	/**
	 * EmbedBuild with cyan color, for any Embed I have to use, with field integration
	 * 
	 * @param name = field name
	 * @param value = value of the field
	 * @param inline = if the field is inline
	 * @return EmbedBuilder
	 */
	public EmbedBuilder embed(String name, String value, boolean inline) {
		return new EmbedBuilder().setColor(Color.cyan).addField(new Field(name, value, inline)).setFooter(OldForgeBOT.getBotName(), "https://dl.alwyn974.re/modding_forge_old.png");
	}
	
	/**
	 * EmbedBuild with cyan color, for any Embed I have to use, with description integration
	 * 
	 * @param description = the description 
	 * @return EmbedBuilder
	 */
	public EmbedBuilder embed(String description) {
		return new EmbedBuilder().setColor(Color.cyan).setDescription(description).setTimestamp(OffsetDateTime.now()).setFooter(OldForgeBOT.getBotName(), "https://dl.alwyn974.re/modding_forge_old.png");
	}
}