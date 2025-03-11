package robin.discordbot.tutorial.X;// Import classes:
import com.twitter.clientlib.TwitterCredentialsBearer;
import com.twitter.clientlib.api.TwitterApi;

public class XClientExample {
    public static void main(String[] args) {
        // Instantiate library client
        TwitterApi apiInstance = new TwitterApi();

        // Instantiate auth credentials (App-only example)
        TwitterCredentialsBearer credentials = new TwitterCredentialsBearer(System.getenv("APP-ONLY-ACCESS-TOKEN"));

        // Pass credentials to library client
        apiInstance.setTwitterCredentials(credentials);

    }
} 