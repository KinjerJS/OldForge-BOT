package re.alwyn974.oldforge.bot.cmds.modding;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import re.alwyn974.oldforge.bot.OldForgeBOT;
import re.alwyn974.oldforge.bot.cmds.gson.ForgeTutorial;
import re.alwyn974.oldforge.bot.cmds.utils.Command;

/**
 * Tutorial Command
 * 
 * @author <a href="https://github.com/alwyn974"> Alwyn974</a>
 * @version 1.0.6
 * @since 1.0.3
 */
public class TutorialCmd extends Command {

	@Override
	public String getName() {
		return "tutorial";
	}

	@Override
	public String getDescription() {
		return "Affiche une liste des tutoriels disponibles en fonction des arguments";
	}

	@Override
	public String getUsage() {
		return String.format("%s%s `[-v<mcversion>] <sujet>`", getPrefix(), getName());
	}

	@Override
	public Permission getPermission() {
		return Permission.MESSAGE_WRITE;
	}

	@Override
	public String[] getAliases() {
		return new String[] { "tutoriel", "tutorials", "tutoriels" };
	}

	@Override
	public void execute(TextChannel channel, User sender, String[] args, Message msg) {
		if (args.length == 0)
			channel.sendMessage(error().setDescription("Utilisation : " + getUsage()).build()).queue();
		else {
			String url = "https://minecraftforgefrance.fr/discordapi/tutorial?term=";
			String tutorialName = "";
			String tutorialVersion = "";
			int beginTutorialName = 0;
			
			if (args[0].startsWith("-v")) {
				tutorialVersion = "&hasTags[]=" + args[0].substring(args[0].indexOf("-v") + 2);
	            beginTutorialName = 1;
			}
			
			for (int i = beginTutorialName; i < args.length; i++)
                tutorialName += (i == (args.length - 1)) ? args[i] : args[i] + "%20";			
                
            HttpsURLConnection connection = null;
    		BufferedReader reader = null;
    		String json = null;
    		try {
    			connection = (HttpsURLConnection) new URL(String.format("%s%s%s", url, tutorialName, tutorialVersion)).openConnection();
    			connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
    			connection.connect();
    		    reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), Charset.forName("UTF-8")));
    		    json = reader.readLine();
    		} catch (IOException e) {
    			OldForgeBOT.e("Erreur : ", e);
    		}
    		
    		JsonElement element = new JsonParser().parse(json);
			JsonObject obj = element.getAsJsonObject();
    		
			if (obj.get("message") != null)
				channel.sendMessage(error().setDescription("Aucun résultat ne correspond à votre recherche").build()).queue();
			else {
				EmbedBuilder builder = embed();
				builder.setTitle("Liste des tutoriels correspondants à votre recherche : ");
				ArrayList<String> fieldsName = new ArrayList<String>();
				
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
					channel.sendMessage(error().setDescription("Votre recherche renvoie trop de résultats, merci de l'affiner.").build()).queue();
				else 
					channel.sendMessage(builder.build()).queue();
			}
		}
	}

}
