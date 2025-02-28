package robin.discordbot2.tutorial.X;

import com.twitter.clientlib.TwitterCredentialsBearer;
import com.twitter.clientlib.api.TwitterApi;

public class XAuthenticationExample {
    public static void main(String[] args) {
        TwitterApi apiInstance = new TwitterApi();

        // 使用环境变量设置 Bearer Token
        String bearerToken = System.getenv("APP-ONLY-ACCESS-TOKEN");
        TwitterCredentialsBearer credentials = new TwitterCredentialsBearer(bearerToken);
        apiInstance.setTwitterCredentials(credentials);

        // 现在您可以使用 apiInstance 调用不需要用户上下文的端点方法
        try {
            // 在这里添加对端点的调用，例如查找推文
        } catch (Exception e) {
            System.err.println("发生异常: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 