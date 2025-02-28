package robin.discordbot2.service.impl;

import org.springframework.stereotype.Service;

import dev.langchain4j.data.message.AudioContent;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.UserMessage;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import robin.discordbot2.service.MainChannelAIService;

@Service
public class MainChannelAIServiceImplTest implements MainChannelAIService {
    private Dotenv dotenv = Dotenv.load();

    //  创建一个全局共享的 ChatMemory 实例
    private final ChatMemory chatMemory = MessageWindowChatMemory.builder().maxMessages(100).build();

    private String getGeminiToken(){
        return dotenv.get("GEMINI2");
    }

    private static String systemMessage = """
                summarize the given audio
                """;
    interface AiPlayGroundTest {
        String chat(@UserMessage AudioContent audio) throws NoSuchMethodException;
    }

    @Override
    public String aiPlayGroundTest(MessageReceivedEvent event) throws NoSuchMethodException {

        ChatLanguageModel model = GoogleAiGeminiChatModel.builder()
                .apiKey(getGeminiToken()) // 需要替换为你的 Gemini API 密钥 [2]
                .modelName("gemini-2.0-flash") //  模型名称 [2]
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
        AudioContent audioContent = AudioContent.from("https://pub-d2e4cfca78f042f29331f4f9fcf74111.r2.dev/%E5%BD%95%E7%94%A8%E6%B3%A8%E6%84%8F%E4%BA%8B%E9%A1%B9.mp3");
        String chat = aiPlayGroundTest.chat(audioContent);
        System.out.println(chat);

        //todo: make an agent with function calling feature
        return chat;
    }


    @Override
    public String aiPlayGround(MessageReceivedEvent event) {
        return null;
    }
}
