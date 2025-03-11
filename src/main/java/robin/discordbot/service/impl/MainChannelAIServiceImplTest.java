package robin.discordbot.service.impl;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.UserMessage;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import robin.discordbot.mapper.AiMapper;
import robin.discordbot.pojo.entity.aiPrompt;
import robin.discordbot.service.MainChannelAIService;

import java.time.LocalDateTime;

@Service
public class MainChannelAIServiceImplTest implements MainChannelAIService {
    @Autowired
    private AiMapper aiMapper;
    private Dotenv dotenv = Dotenv.load();

    //  创建一个全局共享的 ChatMemory 实例
    private final ChatMemory chatMemory = MessageWindowChatMemory.builder().maxMessages(100).build();

    private String getGeminiToken() {
        return dotenv.get("GEMINI2");
    }


    interface AiPlayGroundTest {
        String chat(@UserMessage String message) throws NoSuchMethodException;
    }

    @Override
    public String aiPlayGroundTest(MessageReceivedEvent event) throws NoSuchMethodException {
        String contentRaw = event.getMessage().getContentRaw();
        String systemMessage = "";
        String modelName = "";
        if (contentRaw != null && contentRaw.startsWith("@M ")) {
            modelName = contentRaw.substring(3);
            String globalName = event.getAuthor().getGlobalName();
            LocalDateTime now = LocalDateTime.now();
            aiPrompt aiPrompt = new aiPrompt(1, globalName, now, modelName);
            aiMapper.updateAI(aiPrompt, 1);
            return "ModelName updated: " + modelName;
        }
        if (contentRaw != null && contentRaw.startsWith("@S ")) {
            systemMessage = contentRaw.substring(3);
            String globalName = event.getAuthor().getGlobalName();
            LocalDateTime now = LocalDateTime.now();
            aiPrompt aiPrompt = new aiPrompt(1, systemMessage, globalName, now);
            aiMapper.updateAI(aiPrompt, 1);
            chatMemory.clear();
            return "SystemPrompt updated: " + systemMessage;
        } else {
            aiPrompt aiEntity = aiMapper.getAiById(1);

            if(aiEntity.getModelName().isEmpty() || aiEntity.getPrompt().isEmpty()) {
                aiPrompt aiPrompt = new aiPrompt();
                aiPrompt.setModelName("gemini-2.0-flash");
                aiPrompt.setPrompt("Hello, I am Gemini. How can I help you?");
                aiPrompt.setCreationTime(LocalDateTime.now());
                aiMapper.updateAI(aiPrompt,1);
                aiEntity = aiMapper.getAiById(1);
            }
            modelName = aiEntity.getModelName();
            systemMessage = aiEntity.getPrompt();
        }

        ChatLanguageModel model = GoogleAiGeminiChatModel.builder()
                .apiKey(getGeminiToken()) // 需要替换为你的 Gemini API 密钥 [2]
                .modelName(modelName) //  模型名称 [2]
                .build();

        String finalSystemMessage = systemMessage;
        System.out.println("finalSystemMessage: " + finalSystemMessage);
        System.out.println("modelName: " + modelName);
        AiPlayGroundTest aiPlayGroundTest = AiServices.builder(AiPlayGroundTest.class)
                .chatMemory(chatMemory)
                .chatLanguageModel(model)
                .tools()
                .systemMessageProvider(id -> finalSystemMessage)
                .build();

        String chat = aiPlayGroundTest.chat(contentRaw);
        return chat;
    }

    record WeatherForecast(
            String location,
            String forecast,
            int temperature) {}

    @Tool("Get the weather forecast for a location")
    WeatherForecast getForecast(@P("Location to get the forecast for") String location) {
        if (location.equals("Paris")) {
            return new WeatherForecast("Paris", "sunny", 20);
        } else if (location.equals("London")) {
            return new WeatherForecast("London", "rainy", 15);
        } else if (location.equals("Tokyo")) {
            return new WeatherForecast("Tokyo", "warm", 32);
        } else {
            return new WeatherForecast("Unknown", "unknown", 0);
        }
    }


    @Override
    public String aiPlayGround(MessageReceivedEvent event) {
        return null;
    }
}
