package robin.discordbot2.tutorial.X;

import com.twitter.clientlib.ApiException;
import com.twitter.clientlib.api.TwitterApi;
import com.twitter.clientlib.model.*;
import com.twitter.clientlib.TwitterCredentialsBearer;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class XEndpointExample {
    public static void main(String[] args) {

        TwitterApi apiInstance = new TwitterApi();
        TwitterCredentialsBearer credentials = new TwitterCredentialsBearer(System.getenv("APP-ONLY-ACCESS-TOKEN"));
        apiInstance.setTwitterCredentials(credentials);

        String id = "1511757922354663425"; // String | A single Tweet ID.
        Set<String> expansions = new HashSet<>(Arrays.asList("author_id")); // Set<String> | A comma separated list of fields to expand.
        Set<String> tweetFields = new HashSet<>(Arrays.asList("created_at", "lang", "context_annotations")); // Set<String> | A comma separated list of Tweet fields to display.
        Set<String> userFields = new HashSet<>(Arrays.asList("created_at", "description", "name")); // Set<String> | A comma separated list of User fields to display.

        try {
             SingleTweetLookupResponse result = apiInstance.tweets().findTweetById(id, expansions, tweetFields, userFields, null, null, null);
             System.out.println(result);
        } catch (ApiException e) {
             System.err.println("Exception when calling TweetsApi#findTweetById");
             System.err.println("Status code: " + e.getCode());
             System.err.println("Reason: " + e.getResponseBody());
             System.err.println("Response headers: " + e.getResponseHeaders());
             e.printStackTrace();
        }
    }
} 