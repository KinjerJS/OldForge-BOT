package re.alwyn974.oldforge.bot.cmds.modding;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import re.alwyn974.oldforge.bot.OldForgeBOT;
import re.alwyn974.oldforge.bot.cmds.gson.ForgeEvents;
import re.alwyn974.oldforge.bot.cmds.utils.Command;

/**
 * Events Command
 * 
 * @author <a href="https://github.com/alwyn974"> Alwyn974</a>
 * @version 1.0.0
 * @since 1.0.0
 */
public class EventsCmd extends Command {

	@Override
	public String getName() {
		return "events";
	}

	@Override
	public String getDescription() {
		return "Affiche une liste des events disponible en fonction des arguments";
	}

	@Override
	public String getUsage() {
		return String.format("%s%s `<eventName>`", getPrefix(), getName());
	}

	@Override
	public Permission getPermission() {
		return Permission.MESSAGE_WRITE;
	}

	@Override
	public String[] getAliases() {
		return new String[] {"event"};
	}

	@Override
	public void execute(TextChannel channel, User sender, String[] args, Message msg) {
		if (args.length == 0)
			channel.sendMessage(error().setDescription("Utilisation : " + getUsage()).build()).queue();
		else {
			String url = "https://minecraftforgefrance.fr/";
			String eventName = "";
			
			for (int i = 0; i < args.length; i++) {
				eventName += (i == (args.length -1)) ? args[i] : args[i] + "%20";
			}
			
			HttpsURLConnection connection = null;
			BufferedReader reader = null;
			String json = null;
			try {
				connection = (HttpsURLConnection) new URL(String.format("%sdiscordapi/forgeevents?term=%s", url, eventName)).openConnection();
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
				channel.sendMessage(error().setDescription("Il n'existe aucun event avec ce nom.").build()).queue();
			else {
				EmbedBuilder builder = embed();
				builder.setTitle("Liste des events correspondants à votre recherche : ");
				ArrayList<String> fieldsName = new ArrayList<String>();
				
				obj.entrySet().forEach(o -> {
					ForgeEvents event = new Gson().fromJson(o.getValue().getAsJsonObject(), ForgeEvents.class);
					String description = event.getDescription().length() >= 512 - 5 ? event.getDescription().substring(0, 512 - 5) + "(...)" : event.getDescription();
					String anchorUrl = String.format("%sforgeevents#%s", url, event.getAnchors());
					fieldsName.add(o.getKey());
					builder.addField(o.getKey(), String.format("**- Package** : `%s`\n** - Description** : %s\n[Pour plus d'info](%s)", event.getPackage(), description, anchorUrl), false);
				});
				
				if (fieldsName.size() > 25 || builder.length() >= 6000) 
					channel.sendMessage(error().setDescription("Votre recherche renvoie trop de résultats, merci de l'affiner.").build()).queue();
				else 
					channel.sendMessage(builder.build()).queue();
			}
		}
	}

}
