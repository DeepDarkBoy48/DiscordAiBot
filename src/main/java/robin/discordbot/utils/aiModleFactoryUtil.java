package robin.discordbot.utils;

import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import io.github.cdimascio.dotenv.Dotenv;

public class aiModleFactoryUtil {
    private static Dotenv dotenv = Dotenv.load();
    static private String getGeminiToken(){
        return dotenv.get("GEMINI2");
    }
    static private String getOpenAiToken(){
        return dotenv.get("openaiToken");
    }

    //get the model of the google ai
    static public GoogleAiGeminiChatModel getGeminiModel(String modelName){
        return GoogleAiGeminiChatModel.builder()
                .apiKey(getGeminiToken())
                .modelName(modelName)
                .build();
    }

    //get the model of the openai
    static public OpenAiChatModel getOpenAiModel(String modelName){
        return OpenAiChatModel.builder()
                .apiKey(getOpenAiToken())
                .modelName(modelName)
                .build();
    }
}
