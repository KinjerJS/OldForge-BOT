package fr.kinjer.oldforge.bot.command;

import fr.kinjer.oldforge.bot.OldForgeBOT;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

import java.awt.*;
import java.time.OffsetDateTime;

public interface Command {

    /**
     * Execute the command
     * @param event = the event
     */
    void execute(SlashCommandInteractionEvent event);

    /**
     * Get the command data
     *
     * @return the command data
     */
    SlashCommandData getData();

    /**
     * Permission to use the command
     *
     * @return permission
     */
    Permission getPermission();

    /**
     * EmbedBuilder with green color, for task successful
     *
     * @return EmbedBuilder
     */
    default EmbedBuilder success() {
        return new EmbedBuilder().setColor(Color.green).setTimestamp(OffsetDateTime.now()).setFooter(OldForgeBOT.getBotName(), OldForgeBOT.getBotProfile());
    }

    /**
     * EmbedBuilder with red color, for task not successful
     *
     * @return EmbedBuilder
     */
    default EmbedBuilder error() {
        return new EmbedBuilder().setColor(Color.red).setTimestamp(OffsetDateTime.now()).setFooter(OldForgeBOT.getBotName(), OldForgeBOT.getBotProfile());
    }

    /**
     * EmbedBuild with cyan color, for any Embed I have to use
     *
     * @return EmbedBuilder
     */
    default EmbedBuilder embed() {
        return new EmbedBuilder().setColor(Color.cyan).setTimestamp(OffsetDateTime.now()).setThumbnail(OldForgeBOT.getBotProfile()).setFooter(OldForgeBOT.getBotName(), OldForgeBOT.getBotProfile());
    }

    /**
     * EmbedBuild with cyan color, for any Embed I have to use, with field integration
     *
     * @param name = field name
     * @param value = value of the field
     * @param inline = if the field is inline
     * @return EmbedBuilder
     */
    default EmbedBuilder embed(String name, String value, boolean inline) {
        return new EmbedBuilder().setColor(Color.cyan).addField(new MessageEmbed.Field(name, value, inline)).setFooter(OldForgeBOT.getBotName(), OldForgeBOT.getBotProfile());
    }

    /**
     * EmbedBuild with cyan color, for any Embed I have to use, with description integration
     *
     * @param description = the description
     * @return EmbedBuilder
     */
    default EmbedBuilder embed(String description) {
        return new EmbedBuilder().setColor(Color.cyan).setDescription(description).setTimestamp(OffsetDateTime.now()).setFooter(OldForgeBOT.getBotName(), OldForgeBOT.getBotProfile());
    }

}
