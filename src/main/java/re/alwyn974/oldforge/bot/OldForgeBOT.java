package re.alwyn974.oldforge.bot;

import java.util.Set;

import javax.security.auth.login.LoginException;

import org.reflections.Reflections;

import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.ShutdownEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import re.alwyn974.logger.BasicLogger;
import re.alwyn974.logger.LoggerFactory;
import re.alwyn974.oldforge.bot.cmds.utils.Command;
import re.alwyn974.oldforge.bot.cmds.utils.CommandManager;

/**
 * Main class of the bot
 * 
 * @author <a href="https://github.com/alwyn974"> Alwyn974</a>
 * @version 1.0.3
 * @since 1.0.0
 */
public class OldForgeBOT extends ListenerAdapter {

	private static JDA jda;
	private static final String TOKEN = "TOKEN";
	private static final String VERSION = "v1.0.5";
	private static String gameActivity = "!help | " + VERSION;
	private static final String BOTNAME = "Modding Forge à l'ancienne";
	private static OldForgeBOT instance;

	private static final BasicLogger LOGGER = LoggerFactory.getLogger("OldForge-BOT");

	public static void main(String[] args) throws InterruptedException, LoginException {
		jda = new JDABuilder(AccountType.BOT).setToken(TOKEN).build();
		jda.getPresence().setStatus(OnlineStatus.ONLINE);
		jda.getPresence().setActivity(Activity.playing(gameActivity));
		jda.addEventListener(instance = new OldForgeBOT());
		jda.awaitReady();

		getInstance().init();
	}

	public void init() {
		i("===============BOT-INFOS===============");
		i("Type = %s", jda.getAccountType());
		i("Status = %s", jda.getPresence().getStatus());
		i("Version = %s", getVersion());
		i("Activity = %s", jda.getPresence().getActivity());
		i("Prefix = %s", getPrefix());

		i("===============Guilds================");
		jda.getGuilds().forEach(g -> {
			g.getSelfMember().getRoles().forEach(r -> {
				i("Guild : %s Permissions : %s with role : %s", g, r.getPermissions(), r.getName());
			});
		});
		i("===============COMMANDS================");

		Reflections reflections = new Reflections("re.alwyn974.oldforge.bot");
		Set<Class<? extends Command>> classes = reflections.getSubTypesOf(Command.class);

		classes.forEach(cmd -> {
			try {
				CommandManager.registerCommand(cmd.newInstance());
			} catch (InstantiationException | IllegalAccessException e) {
				e("Cannot instantiate the command : %s", e);
			}
		});
		i("=============COMMANDS-ALIAS============");
		CommandManager.loadAliases();
	}

	@Override
	public void onShutdown(ShutdownEvent event) {
		jda.shutdown();
	}

	public static String getPrefix() {
		return "!";
	}

	public static JDA getJDA() {
		return jda;
	}

	public static OldForgeBOT getInstance() {
		return instance;
	}

	public static String getGameActivity() {
		return gameActivity;
	}

	public static String getVersion() {
		return VERSION;
	}

	public static String getBotName() {
		return BOTNAME;
	}

	public static void i(String msg, Object... args) {
		LOGGER.info(msg, args);
	}

	public static void s(String msg, Object... args) {
		LOGGER.success(msg, args);
	}

	public static void w(String msg, Object... args) {
		LOGGER.warning(msg, args);
	}

	public static void e(String msg, Object... args) {
		LOGGER.error(msg, args);
	}

	public static void d(String msg, Object... args) {
		LOGGER.debug(msg, args);
	}

	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent e) {
		if (!e.getAuthor().isBot()) {
			TextChannel chan = e.getChannel();
			User sender = e.getAuthor();
			Message msg = e.getMessage();
			String message = e.getMessage().getContentRaw();
			new CommandManager().exec(chan, sender, message, msg);
		}
	}

}
