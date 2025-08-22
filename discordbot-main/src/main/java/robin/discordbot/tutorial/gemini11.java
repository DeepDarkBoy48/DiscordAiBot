package robin.discordbot.tutorial;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.UserMessage;
import org.springframework.stereotype.Service;

@Service
public class gemini11 {
    public static void main(String[] args) throws NoSuchMethodException {
        interface AiPlayGroundTest {
            String chat(@UserMessage String message) throws NoSuchMethodException;
        }

        ChatLanguageModel model = GoogleAiGeminiChatModel.builder()
                .apiKey("AIzaSyBIhh9zabHabc3Y2OaMkET-ceBB1xJIC4w") // 需要替换为你的 Gemini API 密钥 [2]
                .modelName("gemini-2.0-flash") //  模型名称 [2]
                .build();

        AiPlayGroundTest aiPlayGroundTest = AiServices.builder(AiPlayGroundTest.class)
                .chatLanguageModel(model)// 系统提示词 [2]
                .build();
        System.out.println(aiPlayGroundTest.chat("你写一个jojo的梗"));
        

    }
}