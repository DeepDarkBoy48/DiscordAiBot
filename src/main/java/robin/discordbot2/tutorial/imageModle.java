package robin.discordbot2.tutorial;

import dev.langchain4j.model.image.ImageModel;
import dev.langchain4j.model.openai.OpenAiImageModel;
import io.github.cdimascio.dotenv.Dotenv;
import robin.discordbot2.record.llmModel;

public class imageModle {
    public static Dotenv dotenv = Dotenv.load();

    public static String getGeminiToken() {
        return dotenv.get("GEMINI2");
    }

    public static void main(String[] args) {

        ImageModel model = OpenAiImageModel.builder()
                .apiKey(getGeminiToken())
                .modelName(llmModel.GEMINI_FLASH.getModle())
                .build();
    }
}
