package fr.kinjer.oldforge.bot.command.model.modding;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import fr.kinjer.oldforge.bot.OldForgeBOT;
import fr.kinjer.oldforge.bot.command.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import fr.kinjer.oldforge.bot.command.model.gson.ForgeTutorial;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;

public class ModHelpCmd implements Command {

	@Override
	public void execute(SlashCommandInteractionEvent event) {
		OptionMapping optionName = event.getOption("name");
		OptionMapping optionVersion = event.getOption("version");
		String url = "https://minecraftforgefrance.fr/discordapi/solvedthread?term=";
		String tutorialVersion = "";

		if (optionVersion != null) {
			tutorialVersion = "&hasTags[]=" + optionVersion.getAsString() + 2;
		}

		HttpsURLConnection connection;
		BufferedReader reader;
		String json;
		try {
			if(optionName == null)
				throw new NullPointerException("Option name est vide");
			connection = (HttpsURLConnection) new URL(url + optionName.getAsString() + tutorialVersion).openConnection();
			connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
			connection.connect();
			reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
			json = reader.readLine();
		} catch (IOException e) {
			OldForgeBOT.e("Erreur : ", e);
			return;
		}

		JsonElement element = JsonParser.parseString(json);
		JsonObject obj = element.getAsJsonObject();

		if (obj.get("message") != null)
			event.replyEmbeds(error().setDescription("Il n'existe aucune demande d'aide résolu avec ce sujet").build()).queue();
		else {
			EmbedBuilder builder = embed();
			builder.setTitle("Liste des sujets résolus correspondants à votre recherche : ");
			ArrayList<String> fieldsName = new ArrayList<>();

			obj.entrySet().forEach(o -> {
				if (o.getKey().equals("none") && o.getValue().getAsJsonArray().size() == 0)
					return;
				JsonArray arrays = o.getValue().getAsJsonArray();
				fieldsName.add(o.getKey());
				StringBuilder strBuilder = new StringBuilder();
				StringBuilder tooMuch = new StringBuilder();
				int size = 0;
				for (JsonElement array : arrays) {
					ForgeTutorial tuto = new Gson().fromJson(array.getAsJsonObject(), ForgeTutorial.class);
					String field = String.format("[%s](%s)", tuto.getTitle(), tuto.getUrl());
					if ((strBuilder.length() + field.length()) >= 1024) {
						tooMuch.append(field);
						size++;
						tooMuch.append(size != arrays.size() ? "\n" : "");
					} else {
						strBuilder.append(field);
						size++;
						strBuilder.append(size != arrays.size() ? "\n" : "");
					}
				}
				if (tooMuch.length() > 0)
					builder.addField(o.getKey(), tooMuch.toString(), false);

				builder.addField(o.getKey(), strBuilder.toString(), false);

			});

			if (fieldsName.size() > 25 || builder.length() >= 6000)
				event.replyEmbeds(error().setDescription("Votre recherche renvoie trop de résultats, merci de l'affiner.").build()).queue();
			else
				event.replyEmbeds(builder.build()).queue();
		}
	}

	@Override
	public SlashCommandData getData() {
		return new CommandDataImpl("modhelp", "Affiche une liste des demande d'aides résolus en fonction des arguments")
				.addOption(OptionType.STRING, "name", "Nom du sujet de l'aide", true)
				.addOption(OptionType.STRING, "version", "Version de Minecraft");
	}

	@Override
	public Permission getPermission() {
		return null;
	}

}
