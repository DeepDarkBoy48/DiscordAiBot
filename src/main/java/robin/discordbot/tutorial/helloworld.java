package robin.discordbot.tutorial;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import io.github.cdimascio.dotenv.Dotenv;
import robin.discordbot.record.llmModel;

public class helloworld {

    public static Dotenv dotenv = Dotenv.load();

    public static String getGeminiToken() {
        return dotenv.get("GEMINI2");
    }

    public static void main(String[] args) {
        String model = llmModel.GEMINI_FLASH_LITE.getModle();
        ChatLanguageModel modle = GoogleAiGeminiChatModel.builder()
                .apiKey(getGeminiToken())
                .modelName(model)
                .build();

        String answer =  modle.generate("how to use langchain4j");
        String translator = modle.generate("translate {{"+answer+"}} to zh");
        System.out.println(translator);
    }
}
