package robin.discordbot2;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class main {


	public static void main(String[] args) {
		SpringApplication.run(main.class, args);
	}

//	@PostConstruct
//	public void init() {
//		// 设置 HTTP 代理
//		Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 7892));
//		OkHttpClient.Builder builder = new OkHttpClient.Builder().proxy(proxy);
//
//		// 设置 WebSocket 代理
//		WebSocketFactory factory = new WebSocketFactory();
//		factory.getProxySettings().setHost("127.0.0.1").setPort(7892);
//
//		//读取.env配置文件
////		Dotenv config = Dotenv.configure().load();
////		String token = config.get("TOKEN");
//
//		// 使用 token 初始化 bot
//		DefaultShardManagerBuilder deepdarkbot = DefaultShardManagerBuilder.createDefault(token)
//				.setHttpClientBuilder(builder)
//				.setWebsocketFactory(factory);
//
//		// 设置 bot 的状态和活动
//		deepdarkbot.setActivity(Activity.playing("Genshin"));
//		deepdarkbot.setStatus(OnlineStatus.ONLINE);
//		//设置缓存，策略所有成员的缓存
//		deepdarkbot.setMemberCachePolicy(MemberCachePolicy.ALL);
//		//让bot在启动时缓存
//		deepdarkbot.setChunkingFilter(ChunkingFilter.ALL);
//		//缓存的内容，在线状态
//		deepdarkbot.enableCache(CacheFlag.ONLINE_STATUS);
//
//		// 添加事件监听器和 intents
//		deepdarkbot.addEventListeners(new MyListener(),new CommandManager());
//		deepdarkbot.enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT,GatewayIntent.GUILD_PRESENCES);
//
//		// 构建并启动 shard manager
//		ShardManager shardManager = deepdarkbot.build();
//	}

	//部署
//	@PostConstruct
//	public void init() {
//		Dotenv dotenv = Dotenv.load();
//		String discordToken = dotenv.get("discordToken");
//		// 使用 token 初始化 bot
//		DefaultShardManagerBuilder deepdarkbot = DefaultShardManagerBuilder.createDefault(discordToken);
//		// 设置 bot 的状态和活动
//		deepdarkbot.setActivity(Activity.playing("Genshin"));
//		deepdarkbot.setStatus(OnlineStatus.ONLINE);
//		//设置缓存，策略所有成员的缓存
//		deepdarkbot.setMemberCachePolicy(MemberCachePolicy.ALL);
//		//让bot在启动时缓存
//		deepdarkbot.setChunkingFilter(ChunkingFilter.ALL);
//		//缓存的内容，在线状态
//		deepdarkbot.enableCache(CacheFlag.ONLINE_STATUS);
//
//		// 添加事件监听器和 intents
//		deepdarkbot.addEventListeners(new MyListener(),new CommandManager());
//		deepdarkbot.enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT,GatewayIntent.GUILD_PRESENCES);
//
//		// 构建并启动 shard manager
//		ShardManager shardManager = deepdarkbot.build();
//	}
}


