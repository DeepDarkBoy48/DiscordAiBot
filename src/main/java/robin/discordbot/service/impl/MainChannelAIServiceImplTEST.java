package robin.discordbot.service.impl;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.stereotype.Service;
import robin.discordbot.service.MainChannelAIService;

import java.util.List;

@Service
public class MainChannelAIServiceImplA implements MainChannelAIService {

    private Dotenv dotenv = Dotenv.load();

    //  创建一个全局共享的 ChatMemory 实例
    private final ChatMemory chatMemory = MessageWindowChatMemory.builder().maxMessages(100).build();
    private final ChatMemory chatMemory2 = MessageWindowChatMemory.builder().maxMessages(100).build();
    private String getGeminiToken(){
        return dotenv.get("GEMINI2");
    }

    @Override
    public String aiPlayGround(MessageReceivedEvent event) {
        String message = event.getMessage().getContentRaw();

        ChatLanguageModel model = GoogleAiGeminiChatModel.builder()
                .apiKey(getGeminiToken()) // 需要替换为你的 Gemini API 密钥 [2]
                .modelName("gemini-2.0-flash-thinking-exp-01-21") //  模型名称 [2]
                .build();
        ChatMessage systemMessage = new SystemMessage("不准使用”搁这儿“这三个字，你是一个喜欢骂人的人，无论用户问你什么，你都要骂他，还经常使用网络抽象梗嘲讽人，但是骂完还是要提供答案，也要在答案中偷偷骂，把用户骂爽了，再最后一段的内容是解释本次回复中提到的骂人梗，一定用换一个段落，并在开头写“梗指南”这三个字用md加粗斜体格式，表示这一段是解释说明。"); // SystemMessage
        chatMemory.add(systemMessage);
        UserMessage userMessage = new UserMessage(message); //  创建 UserMessage
        chatMemory.add(userMessage); // 添加 UserMessage 到 chatMemory

        List<ChatMessage> chatMessages = chatMemory.messages();

        ChatResponse response = null;
        try {
            response = model.chat(chatMessages); // chatMemory.messages() 现在包含 SystemMessage 和 UserMessage
        } catch (Exception e) {
            System.out.println("shit");
        } finally {
            System.out.println("success");
        }
        AiMessage aiMessage = response.aiMessage();
        chatMemory.add(aiMessage); // 将 AI 的回复消息添加到 chatMemory

        List<ChatMessage> messages = chatMemory.messages();
        String text = messages.get(1).text();
        return aiMessage.text();
    }

    @Override
    public String aiPlayGroundTest(MessageReceivedEvent event) {
        return null;
    }
}
