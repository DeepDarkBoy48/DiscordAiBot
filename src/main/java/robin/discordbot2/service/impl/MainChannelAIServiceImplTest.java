package robin.discordbot2.service.impl;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.agent.tool.ToolSpecifications;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.stereotype.Service;
import robin.discordbot2.pojo.entity.aiEntity.AiMessageFormat;
import robin.discordbot2.pojo.entity.aiEntity.AiMessageFormatTool;
import robin.discordbot2.record.llmModel;
import robin.discordbot2.service.MainChannelAIService;
import robin.discordbot2.utils.aiModleFactoryUtil;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MainChannelAIServiceImplTest implements MainChannelAIService {
    private Dotenv dotenv = Dotenv.load();

    //  创建一个全局共享的 ChatMemory 实例
    private final ChatMemory chatMemory = MessageWindowChatMemory.builder().maxMessages(100).build();

    private String getGeminiToken(){
        return dotenv.get("GEMINI2");
    }

    private static String systemMessage = """
                You are a helpful assistant.
                you should be humorous and cute.
                """;
    interface AiPlayGroundTest {
        String chat(@UserMessage String message) throws NoSuchMethodException;
    }

    @Override
    public String aiPlayGroundTest(MessageReceivedEvent event) throws NoSuchMethodException {

        ChatLanguageModel model = GoogleAiGeminiChatModel.builder()
                .apiKey(getGeminiToken()) // 需要替换为你的 Gemini API 密钥 [2]
                .modelName("gemini-2.0-flash-thinking-exp-01-21") //  模型名称 [2]
                .build();
        AiPlayGroundTest aiPlayGroundTest = AiServices.builder(AiPlayGroundTest.class)
                .chatMemory(chatMemory)
                .chatLanguageModel(model)
                .systemMessageProvider( id -> systemMessage)
                .build();

        String contentRaw = event.getMessage().getContentRaw();
        if (contentRaw.charAt(0) == '@'){
            systemMessage = contentRaw.substring(1);
            return "system message set to: " + systemMessage;
        }
        String chat = aiPlayGroundTest.chat(contentRaw);
        System.out.println(chat);

        //todo: make an agent with function calling feature
        return chat;
    }


    @Override
    public String aiPlayGround(MessageReceivedEvent event) {
        return null;
    }
}
