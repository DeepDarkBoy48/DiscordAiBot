package robin.discordbot2;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.springframework.stereotype.Service;
import robin.discordbot2.commond.CommandManager;
import robin.discordbot2.listener.ChannelListener;
import robin.discordbot2.listener.ForumListener;
import robin.discordbot2.listener.ReactionListener;

@Slf4j
@Service
public class DiscordBotService {

    private ShardManager shardManager;

    @PostConstruct
    public void init() {
        try {
            Dotenv dotenv = Dotenv.load();
            String discordToken = dotenv.get("discordToken");

            // 初始化 ShardManager
            DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(discordToken)
                    .setActivity(Activity.playing("Genshin"))
                    .setStatus(OnlineStatus.ONLINE)
                    .setMemberCachePolicy(MemberCachePolicy.ALL)
                    .setChunkingFilter(ChunkingFilter.ALL)
                    .enableCache(CacheFlag.ONLINE_STATUS,CacheFlag.FORUM_TAGS,CacheFlag.ROLE_TAGS)
                    .enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_PRESENCES,GatewayIntent.GUILD_MESSAGE_REACTIONS)
                    .addEventListeners(new ForumListener(), new CommandManager(),new ReactionListener(),new ChannelListener());

            // 启动 ShardManager
            this.shardManager = builder.build();
            log.info("Discord Bot initialized successfully.");
        } catch (Exception e) {
            log.error("Failed to initialize Discord Bot", e);
        }
    }

    @PreDestroy
    public void shutdown() {
        if (shardManager != null) {
            shardManager.shutdown();
            log.info("Discord Bot shutdown successfully.");
        }
    }
}
