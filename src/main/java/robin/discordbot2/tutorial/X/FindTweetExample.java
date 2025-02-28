package robin.discordbot2.tutorial.X;

import com.twitter.clientlib.ApiException;
import com.twitter.clientlib.api.TwitterApi;
import com.twitter.clientlib.model.*;
import com.twitter.clientlib.TwitterCredentialsBearer;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class FindTweetExample {
    public static void main(String[] args) {
        TwitterApi apiInstance = new TwitterApi();
        String bearerToken = System.getenv("APP-ONLY-ACCESS-TOKEN"); // 从环境变量获取 Bearer Token
        TwitterCredentialsBearer credentials = new TwitterCredentialsBearer(bearerToken);
        apiInstance.setTwitterCredentials(credentials);

        String tweetId = "1511757922354663425"; // 要查找的推文 ID
        Set<String> expansions = new HashSet<>(Arrays.asList("author_id"));
        Set<String> tweetFields = new HashSet<>(Arrays.asList("created_at", "lang", "text"));
        Set<String> userFields = new HashSet<>(Arrays.asList("created_at", "description", "username"));

        try {
            SingleTweetLookupResponse result = apiInstance.tweets().findTweetById(tweetId, expansions, tweetFields, userFields, null, null, null);

            // 检查结果是否包含数据
            if (result.getData() != null) {
                System.out.println("Tweet ID: " + result.getData().getId());
                System.out.println("Text: " + result.getData().getText());
                System.out.println("Created At: " + result.getData().getCreatedAt());
                System.out.println("Language: " + result.getData().getLang());

                // 检查是否包含用户信息
                if (result.getIncludes() != null && result.getIncludes().getUsers() != null) {
                    result.getIncludes().getUsers().forEach(user -> {
                        System.out.println("Author Username: " + user.getUsername());
                        System.out.println("Author Description: " + user.getDescription());
                        System.out.println("Author Created At: " + user.getCreatedAt());
                    });
                }
            } else {
                System.out.println("No Tweet found with ID: " + tweetId);
            }

        } catch (ApiException e) {
            System.err.println("Exception when calling TweetsApi#findTweetById");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
    }
} 