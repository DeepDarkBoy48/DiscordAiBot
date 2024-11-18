package robin.discordbot2.config;

import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import robin.discordbot2.pojo.entity.aiEntity.AiMessageFormat;
import robin.discordbot2.pojo.entity.aiEntity.aiSearchOutputEntity;

@Configuration
public class Langchain4j {
    private final Dotenv dotenv = Dotenv.load();

    public String getOpenaiToken(){
        return dotenv.get("openaiToken");
    }
    public String getDiscordToken(){
        return dotenv.get("discordToken");
    }

    //AiAssistantHTmLFigure
    @Bean
    public ChatLanguageModel chatLanguageModelHTMLFigure() {
        return OpenAiChatModel.builder()
                .apiKey(getOpenaiToken())
                .modelName("gpt-4o-mini")
                .build();
    }
    public interface AiAssistantFigure {
        @SystemMessage("Output the answer according to the information. " +
                "If the information is in Chinese, it needs to be translated into English and then the answer needs to be output. " +
                "The output answer should be wrapped in HTML tags, and creativity is needed to enhance the visual effect, and some styles such as colour changes and font sizes should be used. " +
                "The font size of English should be set to Arial. Output more content each time." +
                " For example, if the user enters ‘ai rankings’, a detailed table should be given, including a comparison of various parameters.")
        String chat(@MemoryId Object userId, @UserMessage("AiMessageFormat") AiMessageFormat aiMessageFormat);
    }
    @Bean
    public AiAssistantFigure deepDarkAiHTMLFigure() {
        return AiServices.builder(AiAssistantFigure.class)
                .chatLanguageModel(chatLanguageModelHTMLFigure())
                .build();
    }

    //AiAssistantText
    @Bean
    public ChatLanguageModel chatLanguageModelText() {
        return OpenAiChatModel.builder()
                .apiKey(getOpenaiToken())
                .modelName("gpt-4o-mini")
                .build();
    }
    public interface AiAssistantText {
        @SystemMessage("根据message信息输出答案，并把输出的答案用markdown包装，如大标题，代码块，加粗等")
        String chat(@MemoryId Object userId, @UserMessage("AiMessageFormat") AiMessageFormat aiMessageFormat);
    }
    @Bean
    public AiAssistantText deepDarkAi() {
        return AiServices.builder(AiAssistantText.class)
                .chatLanguageModel(chatLanguageModelText())
                .build();
    }

    //论坛ai
    public interface AiAssistantThreadText {
        @SystemMessage("根据message信息输出答案，并把输出的答案用markdown包装，如大标题，代码块，加粗等")
        String chat(@MemoryId Object userId, @UserMessage("AiMessageFormat") AiMessageFormat aiMessageFormat);
    }

    public interface embed {
        @SystemMessage("根据用户提供的url输出嵌入代码。以下是输出实例：" +
                "1.YouTube用嵌入，用户户输入https://www.youtube.com/embed/w-TT5M6Ax_k?si=NryDdn03edu90Yq7" +
                "（用户可能会输入其他的内容，但是只要识别到是YouTube就获取这个链接，将该链接填入下方代码块的src）" +
                "<div style=\"display: flex; justify-content: center; align-items: center; max-width: 1200px; width: 100%; aspect-ratio: 16/9; margin: 0 auto;\">\n" +
                "    <iframe \n" +
                "        style=\"width: 100%; height: 100%;\"\n" +
                "        src=\"\"\n" +
                "        title=\"YouTube video player\" \n" +
                "        frameborder=\"0\" \n" +
                "        allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\"\n" +
                "        referrerpolicy=\"strict-origin-when-cross-origin\" \n" +
                "        allowfullscreen>\n" +
                "    </iframe>\n" +
                "</div>" +
                "2.qq音乐" +
                "用户输入songid如127570280，那么将这个id填入下方代码块songid中" +
                "<iframe frameborder=\"no\" border=\"0\" marginwidth=\"0\" marginheight=\"0\" width=320 height=65\n" +
                "src=\"https://i.y.qq.com/n2/m/outchain/player/index.html?songid=127570280&songtype=0\"></iframe>" +
                "最后输出代码块给用户,用markdonw格式包装")
        String chat(@MemoryId Object userId, @UserMessage("AiMessageFormat") AiMessageFormat aiMessageFormat);
    }

    @Bean
    public ChatMemoryProvider chatMemoryProvider() {
        return memoryId -> MessageWindowChatMemory.builder()
                .id(memoryId)
                .maxMessages(30)
                .build();
    }
    @Bean
    public AiAssistantThreadText deepDarkTreadAi(ChatMemoryProvider chatMemoryProvider) {
        return AiServices.builder(AiAssistantThreadText.class)
                .chatLanguageModel(chatLanguageModelText())
                .chatMemoryProvider(chatMemoryProvider)
                .build();
    }

    @Bean
    public embed embed(ChatMemoryProvider chatMemoryProvider) {
        return AiServices.builder(embed.class)
                .chatLanguageModel(chatLanguageModelText())
                .chatMemoryProvider(chatMemoryProvider)
                .build();
    }

    //ai search tavily
    @Bean
    public ChatLanguageModel chatLanguageModel4o() {
        return OpenAiChatModel.builder()
                .apiKey(getOpenaiToken())
//                .responseFormat("json_schema")
//                .strictJsonSchema(true)
                .modelName("gpt-4o")
                .build();
    }
    public interface aiSearchTavily {
        @SystemMessage("请根据 resultInfo 中提供的信息同时结合自己本身的知识，重新整理并编辑成一篇文章。文章需满足以下要求：\n" +
                "1. **内容完整性**：尽可能涵盖 resultInfo 中的所有信息同时结合自己本身的知识。\n" +
                "2. **链接整合**：将 resultInfo 中所有的网页链接合理嵌入到文章中，以便读者进一步了解相关内容。示例：如何学习编程？https://example.com/learn-programming\n" +
                "3. **格式优化**：使用markdown语法包裹文章，包括大标题，粗体，斜体，代码块，有序列表，无序列表。\n" +
                "4. **署名**：文章末尾添加斜体加粗文字：_**AI服务由 @Crispy Frog 提供**_ \n" +
                "\n" +
                "此外，根据 resultInfo 提供的信息生成 3 个相关追问问题，并将这些问题填入 aiSearchOutputEntity 的 List<String> buttonInfo 中。\n")
        aiSearchOutputEntity chat(@MemoryId Object userId, @UserMessage String resultInfo);
    }

    @Bean
    public aiSearchTavily aiSearchTavily() {
        return AiServices.builder(aiSearchTavily.class)
                .chatLanguageModel(chatLanguageModel4o())
                .build();
    }
}
