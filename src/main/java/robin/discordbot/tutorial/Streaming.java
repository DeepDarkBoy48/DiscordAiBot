package robin.discordbot.tutorial;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.model.StreamingResponseHandler;
import dev.langchain4j.model.googleai.GoogleAiGeminiStreamingChatModel;
import io.github.cdimascio.dotenv.Dotenv;
import robin.discordbot.record.llmModel;

import java.util.concurrent.TimeUnit;

public class Streaming {
    public static Dotenv dotenv = Dotenv.load();

    public static String getGeminiToken() {
        return dotenv.get("GEMINI2");
    }
    public static void main(String[] args) {

        GoogleAiGeminiStreamingChatModel model = GoogleAiGeminiStreamingChatModel.builder()
                .apiKey(getGeminiToken())
                .modelName(llmModel.GEMINI_FLASH.getModle())
                .build();

        model.generate("你写一个故事", new StreamingResponseHandler<AiMessage>() {
            @Override
            public void onNext(String token) {
                System.out.println(token);
                try {
                    TimeUnit.SECONDS.sleep(3);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onError(Throwable error) {
                System.out.println(error);
            }
        });
    }
}
