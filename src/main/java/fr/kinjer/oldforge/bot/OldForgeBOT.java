package fr.kinjer.oldforge.bot;

import fr.kinjer.oldforge.bot.command.CommandManager;
import fr.kinjer.oldforge.bot.event.EventManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import re.alwyn974.logger.BasicLogger;
import re.alwyn974.logger.LoggerFactory;

import java.util.Arrays;

public class OldForgeBOT {

    private static final String TOKEN = System.getenv("BOT_OLD_FORGE_TOKEN");
    private static final String VERSION = "v2.0.0";
    private static final String GAME_ACTIVITY = "/help | " + VERSION;
    private static final String BOT_NAME = "Modding Forge à l'ancienne";
    private static String botProfile;
    private static OldForgeBOT instance;

    private static final BasicLogger LOGGER = LoggerFactory.getLogger("OldForge-BOT");
    private final EventManager eventManager;
    private final CommandManager commandManager;

    public OldForgeBOT() throws Exception {
        this.commandManager = new CommandManager();
        this.eventManager = new EventManager();

        JDABuilder builder = JDABuilder.createDefault(TOKEN, Arrays.asList(GatewayIntent.values()))
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .setChunkingFilter(ChunkingFilter.NONE)
                .enableCache(Arrays.asList(CacheFlag.values()))
                .setStatus(OnlineStatus.ONLINE)
                .setActivity(Activity.playing(GAME_ACTIVITY));

        this.eventManager.getEvents().forEach(builder::addEventListeners);

        init(builder.build());
    }

    public static void main(String[] args) throws Exception {
        instance = new OldForgeBOT();
    }

    public static void init(JDA jda) {
        i("===============BOT-INFOS===============");
        i("Type = %s", jda.getAccountType());
        i("Status = %s", jda.getPresence().getStatus());
        i("Version = %s", getVersion());
        i("Activity = %s", jda.getPresence().getActivity());
        i("Prefix = %s", getPrefix());
        i("===============BOT-INFOS===============");
        botProfile = jda.getSelfUser().getAvatarUrl();
    }

    public static String getPrefix() {
        return "!";
    }

    public static OldForgeBOT getInstance() {
        return instance;
    }

    public static String getGameActivity() {
        return GAME_ACTIVITY;
    }

    public static String getVersion() {
        return VERSION;
    }

    public static String getBotName() {
        return BOT_NAME;
    }

    public static String getBotProfile() {
        return botProfile;
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

    public CommandManager getCommandManager() {
        return this.commandManager;
    }

    public EventManager getEventManager() {
        return eventManager;
    }
}
