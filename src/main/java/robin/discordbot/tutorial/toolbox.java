package robin.discordbot.tutorial;

import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.output.Response;
import org.springframework.stereotype.Service;
import robin.discordbot.config.Langchain4j;
import robin.discordbot.pojo.entity.aiEntity.AiMessageFormat;

@Service
public class toolbox {

    private static ChatLanguageModel model = Langchain4j.chatLanguageModelGeminiFlashLite();
    private static Langchain4j.AiAssistantGeminiFlashLite aiService = Langchain4j.aiAssistantGeminiFlashLite();
    @Tool
    public String audioAnalysis(String audio) {
        Response<AiMessage> generate = model.generate(UserMessage.from("你是谁"));
        String content = generate.content().text();
        AiMessageFormat aiMessageFormat = aiService.chat("你发个表情");
        String message = aiMessageFormat.getMessage();
        System.out.println(message);
        System.out.println(content);
        return "Audio analysis is not available yet.";
    }

    public static void main(String[] args) {
        Response<AiMessage> generate = model.generate(UserMessage.from("你是谁"));
        String content = generate.content().text();
        AiMessageFormat aiMessageFormat = aiService.chat("你发个表情");
        String message = aiMessageFormat.getMessage();
        System.out.println(content);
        System.out.println(message);

    }

}
