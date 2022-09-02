package fr.kinjer.oldforge.bot.command.model.modding;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import fr.kinjer.oldforge.bot.OldForgeBOT;
import fr.kinjer.oldforge.bot.command.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;
import fr.kinjer.oldforge.bot.command.model.gson.ForgeEvents;

public class EventsCmd implements Command {

	@Override
	public void execute(SlashCommandInteractionEvent event) {
		OptionMapping option = event.getOption("name");
		String url = "https://minecraftforgefrance.fr/";

		HttpsURLConnection connection;
		BufferedReader reader;
		String json;
		try {
			if(option == null)
				throw new NullPointerException("L'option name est vide");
			connection = (HttpsURLConnection) new URL(url + "discordapi/forgeevents?term=" + option.getAsString()).openConnection();
			connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
			connection.connect();
			reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
			json = reader.readLine();
		} catch (Exception e) {
			OldForgeBOT.e("Erreur : ", e);
			event.reply("Une erreur est survenue.").queue();
			return;
		}

		JsonElement element = JsonParser.parseString(json);
		JsonObject obj = element.getAsJsonObject();

		if (obj.get("message") != null) {
			event.replyEmbeds(error().setDescription("Il n'existe aucun event avec ce nom.").build()).queue();
		} else {
			EmbedBuilder builder = embed();
			builder.setTitle("Liste des events correspondants à votre recherche : ");
			ArrayList<String> fieldsName = new ArrayList<>();

			obj.entrySet().forEach(o -> {
				ForgeEvents eventForge = new Gson().fromJson(o.getValue().getAsJsonObject(), ForgeEvents.class);
				String description = eventForge.getDescription().length() >= 512 - 5 ? eventForge.getDescription().substring(0, 512 - 5) + "(...)" : eventForge.getDescription();
				String anchorUrl = String.format("%sforgeevents#%s", url, eventForge.getAnchors());
				fieldsName.add(o.getKey());
				builder.addField(o.getKey(), String.format("**- Package** : `%s`\n** - Description** : %s\n[Pour plus d'info](%s)", eventForge.getPackage(), description, anchorUrl), false);
			});

			if (fieldsName.size() > 25 || builder.length() >= 6000) {
				event.replyEmbeds(error().setDescription("Votre recherche renvoie trop de résultats, merci de l'affiner.").build()).queue();
				return;
			}

			event.replyEmbeds(builder.build()).queue();
		}
	}

	@Override
	public SlashCommandData getData() {
		return new CommandDataImpl("events", "Affiche une liste des events disponible en fonction des arguments")
				.addOption(OptionType.STRING, "name", "Recherche un event par son nom", true);
	}

	@Override
	public Permission getPermission() {
		return null;
	}

}
