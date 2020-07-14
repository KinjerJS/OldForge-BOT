package re.alwyn974.oldforge.bot.handler;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Stream;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Message.Attachment;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import re.alwyn974.oldforge.bot.OldForgeBOT;

/**
 * Hastebin Handler
 * 
 * @author <a href="https://github.com/alwyn974"> Alwyn974</a>
 * @version 1.0.7
 * @since 1.0.7
 */
public class AutoHastebinHandler {

	private static final Set<String> READABLE_FILE_EXTENSION = new HashSet<>(Arrays.asList("txt", "log", "xml", "gradle", "java", "json", "lang"));
	
	private EmbedBuilder error = new EmbedBuilder().setColor(Color.red).setTimestamp(OffsetDateTime.now()).setFooter(OldForgeBOT.getBotName(), "https://dl.alwyn974.re/modding_forge_old.png");
	private EmbedBuilder success = new EmbedBuilder().setColor(Color.green).setTimestamp(OffsetDateTime.now()).setFooter(OldForgeBOT.getBotName(), "https://dl.alwyn974.re/modding_forge_old.png");
	
	public void handleMessage(TextChannel channel, Message msg, User sender) {
		if (msg.getAttachments() != null) {
			for (Attachment attachment : msg.getAttachments()) {
				if (isReadableFile(attachment)) {
					try {
						String link = sendPostRequest(getFileContent(attachment));
						if (link != null) {
							channel.sendMessage(success.setDescription(String.format("Lien hastebin : https://code.alwyn974.re/%s.%s\n"
									+ "Coupable : %s", link, attachment.getFileExtension(), sender.getAsMention())).build()).queue();
						}
					} catch (IOException e) {
						OldForgeBOT.e("Erreur lors de la création du lien hastebin : \n%s", e);
						channel.sendMessage(error.setDescription("Erreur lors de la création du lien hastebin\n" + String.format("➳ Erreur : `%s`", e.getMessage())).build()).queue();
					}
				}
			}
		}
	}

	private boolean isReadableFile(Attachment attachment) {
		String extension = attachment.getFileExtension();
		return extension != null && READABLE_FILE_EXTENSION.contains(extension.toLowerCase());
	}
	
	private String getFileContent(Attachment attachment) {
		CompletableFuture<InputStream> is = attachment.retrieveInputStream();
		BufferedReader br = null;
		Stream<String> contents = null;
		try {
			br = new BufferedReader(new InputStreamReader(is.get(), Charset.forName("UTF-8")));
			contents = br.lines();
		} catch (InterruptedException | ExecutionException e) {
			OldForgeBOT.e("Erreur lors de la lecture du fichier : \n%s", e);
			return "Auto Hastebin by Alwyn974";
		}
		StringBuffer sb = new StringBuffer();
		sb.append("/*Auto Hastebin by Alwyn974*/").append("\n").append("/*With " + OldForgeBOT.getBotName() + " " + OldForgeBOT.getVersion() + "*/").append("\n\n\n");
		contents.forEach(str -> {
			sb.append(str);
			sb.append("\n");
		});
		
		return sb.toString();
	}
	
	private String sendPostRequest(String text) throws IOException  {
		byte[] textBytes = text.getBytes("UTF-8");
		URL serverURL = new URL("https://code.alwyn974.re/documents?");
		HttpURLConnection connection = (HttpURLConnection) serverURL.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Accept-Charset", "UTF-8");
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
        connection.setRequestProperty("Content-Type", "text/plain");
        connection.setRequestProperty("Content-Length", String.valueOf(textBytes.length));
        DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
        wr.write(textBytes, 0, textBytes.length);
        wr.flush();
        wr.close();
        connection.connect();
        
        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), Charset.forName("UTF-8")));
        String response = br.readLine();
        if (response.startsWith("{")) {
        	JsonElement element = new JsonParser().parse(response);
			JsonObject obj = element.getAsJsonObject();
			if (obj.get("key") != null)
				return obj.get("key").getAsString();
        }
        
        return null;
	}

}
