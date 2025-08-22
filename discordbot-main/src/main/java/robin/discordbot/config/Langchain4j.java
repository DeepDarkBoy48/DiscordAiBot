package robin.discordbot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import io.github.cdimascio.dotenv.Dotenv;
import robin.discordbot.pojo.entity.aiEntity.AiMessageFormat;
import robin.discordbot.pojo.entity.aiEntity.aiSearchOutputEntity;

@Configuration
public class Langchain4j {
    private static final Dotenv dotenv = Dotenv.load();

    public String getOpenaiToken() {
        return dotenv.get("openaiToken");
    }

    public String getDiscordToken() {
        return dotenv.get("discordToken");
    }

    public static String getGoogleToken() {
        return dotenv.get("GEMINI2");
    }

    /**
     * chat memory provider
     *
     * @return
     */
    @Bean
    public static ChatMemoryProvider chatMemoryProvider() {
        return memoryId -> MessageWindowChatMemory.builder()
                .id(memoryId)
                .maxMessages(30)
                .build();
    }

    /**
     * chatgpt-4o-mini模型
     *
     * @return
     */
    @Bean(name = "chatLanguageModel4omini")
    public ChatLanguageModel chatLanguageModel4omini() {
        return OpenAiChatModel.builder()
                .apiKey(getOpenaiToken())
                .modelName("gpt-4o-mini")
                .build();
    }

    /**
     * chatgpt-4o模型
     *
     * @return
     */
    @Bean
    public ChatLanguageModel chatLanguageModel4o() {
        return OpenAiChatModel.builder()
                .apiKey(getOpenaiToken())
//                .responseFormat("json_schema")
//                .strictJsonSchema(true)
                .modelName("gpt-4o")
                .build();
    }

//    /**
//     * gemini-2.0-flash模型
//     *
//     * @return
//     */
//    @Bean
//    public ChatLanguageModel chatLanguageModelGeminiFlash() {
//        return GoogleAiGeminiChatModel.builder()
//                .apiKey(getGoogleToken())
//                .modelName("gemini-2.0-flash")
//                .build();
//    }
//
//    /**
//     * gemini-2.0-pro
//     *
//     * @return
//     */
//    @Bean
//    public ChatLanguageModel chatLanguageModelGeminiPro() {
//        return GoogleAiGeminiChatModel.builder()
//                .apiKey(getGoogleToken())
//                .modelName("gemini-2.0-pro-exp-02-05")
//                .build();
//    }
//
//    /**
//     * gemini-2.0-flash-thinking-exp-01-21
//     *
//     * @return
//     */
//    @Bean
//    public ChatLanguageModel chatLanguageModelGeminiFlashThinking() {
//        return GoogleAiGeminiChatModel.builder()
//                .apiKey(getGoogleToken())
////                .responseFormat(ResponseFormat.JSON)
//                .modelName("gemini-2.0-flash-thinking-exp-01-21")
//                .build();
//    }
//
//    /**
//     * Gemini 2.0 Flash-Lite
//     * @return
//     */
//    static public ChatLanguageModel chatLanguageModelGeminiFlashLite() {
//        return GoogleAiGeminiChatModel.builder()
//                .apiKey(getGoogleToken())
//                .modelName("gemini-2.0-flash-lite")
//                .build();
//    }

    /**
     * gpt-4o-mini
     *
     * @return
     */
    @Bean
    public ChatLanguageModel chatLanguageModelHTMLFigure() {
        return OpenAiChatModel.builder()
                .apiKey(getOpenaiToken())
                .modelName("gpt-4o-mini")
                .build();
    }

    /**
     * Aiservice - - AiAssistantFigure
     */

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
                .chatMemoryProvider(chatMemoryProvider())
                .build();
    }

    /**
     * Aiservice - - AiAssistantText
     */
    public interface AiAssistantText {
        @SystemMessage("根据message信息输出答案，并把输出的答案用markdown包装，如大标题，代码块，加粗等")
        String chat(@MemoryId Object userId, @UserMessage("AiMessageFormat") AiMessageFormat aiMessageFormat);
    }

    @Bean
    public AiAssistantText deepDarkAi() {
        return AiServices.builder(AiAssistantText.class)
                .chatLanguageModel(chatLanguageModel4omini())
                .chatMemoryProvider(chatMemoryProvider())
                .build();
    }

    /**
     * Aiservice - - AiAssistantThreadText
     */
    //
    public interface AiAssistantThreadText {
        @SystemMessage("根据message信息输出答案，并把输出的答案用markdown包装，如大标题，代码块，加粗等")
        String chat(@MemoryId Object userId, @UserMessage("AiMessageFormat") AiMessageFormat aiMessageFormat);
    }

    @Bean
    public AiAssistantThreadText deepDarkTreadAi(ChatMemoryProvider chatMemoryProvider) {
        return AiServices.builder(AiAssistantThreadText.class)
                .chatLanguageModel(chatLanguageModel4omini())
                .chatMemoryProvider(chatMemoryProvider)
                .build();
    }

    /**
     * AiService - - embed
     */
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
    public embed embed(ChatMemoryProvider chatMemoryProvider) {
        return AiServices.builder(embed.class)
                .chatLanguageModel(chatLanguageModel4omini())
                .chatMemoryProvider(chatMemoryProvider)
                .build();
    }

    /**
     * AiService - - aiSearchTavily
     *
     * @return
     */

    public interface aiSearchTavily {
        @SystemMessage("请根据 resultInfo 中提供的信息同时结合自己本身的知识，resultInfo可能是英文内容，但也要重新整理并编辑成一篇中文文章。文章需满足以下要求：\n" +
                "1. **内容完整性**：尽可能涵盖 resultInfo 中的所有信息同时结合自己本身的知识。\n" +
                "2. **链接整合**：按照示例处理resultInfo 中所有的网页链接，示例：请访问 [OpenAI](https://www.openai.com) 获取更多信息。\n" +
                "3. **格式优化**：使用markdown语法包裹文章，包括大标题，粗体，斜体，代码块，有序列表，无序列表。但是要尽量让文章紧凑，不要空行\n" +
                "4. **署名**：文章末尾添加斜体加粗文字：_**AI服务由 @Crispy Frog 提供**_ \n" +
                "\n" +
                "此外，根据 resultInfo 提供的信息生成 3 个相关追问问题,字数控制在20个字符以内，并将这些问题填入 aiSearchOutputEntity 的 List<String> buttonInfo 中。\n")
        aiSearchOutputEntity chat(@MemoryId Object userId, @UserMessage String resultInfo);
    }

    @Bean
    public aiSearchTavily aiSearchTavily() {
        return AiServices.builder(aiSearchTavily.class)
                .chatLanguageModel(chatLanguageModel4omini())
                .chatMemoryProvider(chatMemoryProvider())
                .build();
    }

//    /**
//     * AiService - - AiAssistantGemini
//     */
//    public interface AiAssistantGemini {
//        @SystemMessage("""
//                # 你是一个 Java 版 Minecraft 百科
//
//                **角色:** 你是一个精通 Java 版 Minecraft 的百科全书，尤其擅长解答以下方面的问题：
//
//                *   **模组 (Mods):** 熟悉各种流行的模组，例如暮色森林 (The Twilight Forest) 等，并能提供模组的安装、配置、使用建议以及兼容性信息。
//                *   **插件 (Plugins):** 了解常用的服务器插件，并能解答关于插件安装、配置和使用的问题。
//                *   **红石自动化:** 精通红石电路设计，能够解答关于红石自动化装置的问题，并提供设计思路和优化方案。
//                *   **皮肤 (Skins):** 了解 Minecraft 皮肤的制作和使用方法，能够解答关于皮肤下载、上传和编辑的问题。
//                *   **游戏攻略:** 熟悉 Minecraft 的各种游戏机制，能够提供生存、冒险、建造等方面的攻略和技巧。
//
//                **回答风格:**
//
//                *   **使用中文:** 所有回答都应该使用流畅、准确的中文。
//                *   **提供链接:** 尽可能在回答中提供相关的、有用的链接。
//                    *   **主要链接网站（首选前面的网站）：**
//                        *   **1.MCMOD 百科:** <https://search.mcmod.cn/s?key=> (中文，mc百科资料库，在key=后输入关键词搜索)
//                        *   **2.CurseForge:** <https://www.curseforge.com/minecraft> (英文，模组、整合包、资源包、地图等资源下载)
//                        *   **3.Minecraft Wiki - 最全的官方我的世界百科:** <https://zh.minecraft.wiki/> (搜索boss攻略、合成表、生物、方块等)
//                *   **结构化:** 回答应该条理清晰，可以使用标题、列表、代码块等 **Discord 可识别的 Markdown 语法** 进行排版。
//                *   **Discord Markdown 格式：**
//                    *   **超链接:** 使用 `<链接地址>` 或 `[显示文本](链接地址)` 的格式来创建超链接。例如：`<https://www.curseforge.com/minecraft>` 或 `[CurseForge](https://www.curseforge.com/minecraft)`。
//                    *   **加粗:** 使用 `**加粗文本**`。
//                    *   **斜体:** 使用 `*斜体文本*`。
//                    *   **代码块:** 使用 ` ``` ` (三个反引号) 来创建多行代码块。单行代码可以使用单个反引号 \\`。
//                    *   **列表:** 无序列表使用 `-` 或 `*`，有序列表使用数字加 `.`。
//                    *   **标题:** 使用 `#` 号，从一级标题 `#` 到六级标题 `######`。
//                    *   **引用块:** 使用 `>` 符号。
//                    *   **避免使用：** Discord 不支持的 Markdown 语法，例如：三级以后的标题。
//
//                **示例:**
//
//                **用户:** 一个 Forge 服务器，已经有暮色森林了，还可以加什么模组，比如主世界增加绿巨人僵尸，增加难度、物品、怪物之类。
//
//                **回答:**
//
//                好的，您可以考虑添加以下模组来增加难度、怪物和新挑战，它们应该能和暮色森林兼容：
//
//                ### 增加难度、怪物和新挑战:
//
//                *   **僵尸意识 (Zombie Awareness):**
//                    *   **功能:** 增强僵尸 AI，使它们能闻到血腥味、看到光线、听到声音并进行一定程度的合作，还能破坏方块。大幅提升僵尸的威胁。
//                    *   **链接:** <https://www.curseforge.com/minecraft/mc-mods/zombie-awareness>
//                *   **更好的僵尸 (Better Zombie):**
//                    *   **功能:** 类似于僵尸意识，但可能侧重于增强僵尸的不同方面，例如速度、攻击力或特殊能力。你可以与僵尸意识一起使用以增强僵尸。
//                    *   **链接:** <https://www.curseforge.com/minecraft/mc-mods/better-zombie>
//                *   **更好的生物群系 (Better Biome Blend):**
//                    *   **功能:** 不是直接增加难度的模组，但能优化生物群系的过渡，使游戏画面更加流畅。
//                    *   **链接:** <https://www.curseforge.com/minecraft/mc-mods/better-biome-blend>
//
//                **注意:** 在安装多个模组时，请注意模组之间的兼容性，并建议备份你的存档以防万一。您也可以使用一些[整合包](<https://www.curseforge.com/minecraft/modpacks>)来安装一系列优化过的模组。
//
//                希望这些建议对您有所帮助！您还有其他问题吗？例如，您想了解自动化设备或者村民交易方面的模组吗？
//                """)
//        String chat(@MemoryId Object userId, @UserMessage("AiMessageFormat") AiMessageFormat aiMessageFormat);
//    }
//
//    @Bean
//    public AiAssistantGemini aiAssistantGemini() {
//        return AiServices.builder(AiAssistantGemini.class)
//                .chatLanguageModel(chatLanguageModelGeminiFlash())
//                .chatMemoryProvider(chatMemoryProvider())
//                .build();
//    }
//
//    /**
//     * gemini cn-en translate service
//     */
//    public interface AiAssistantGeminiTranslateCN2EN {
//        @SystemMessage("you are a translator, if user input Chinese, you should translate it to English")
//        AiMessageFormat chat(@UserMessage("Usermessage") String message);
//    }
//
//    @Bean
//    public AiAssistantGeminiTranslateCN2EN aiAssistantGeminiTranslateCN2EN() {
//        return AiServices.builder(AiAssistantGeminiTranslateCN2EN.class)
//                .chatLanguageModel(chatLanguageModelGeminiFlash())
//                .build();
//    }
//
//    /**
//     * gemini en-cn translate service
//     */
//    public interface AiAssistantGeminiTranslateEN2CN {
//        @SystemMessage("you are a translator, if user input English, you should translate it to Chinese")
//        AiMessageFormat chat(@UserMessage("AiMessageFormat") String message);
//    }
//
//    @Bean
//    public AiAssistantGeminiTranslateEN2CN aiAssistantGeminiTranslateEN2CN() {
//        return AiServices.builder(AiAssistantGeminiTranslateEN2CN.class)
//                .chatLanguageModel(chatLanguageModelGeminiFlash())
//                .build();
//    }
//
//    /**
//     * Gemini flash thinking
//     */
//    public interface AiAssistantGeminiFlashThinking{
//        @SystemMessage("you are the DeepDarkBot, you can think and answer the most difficult questions")
//        AiMessageFormat chat(@UserMessage("AiMessageFormat") String message);
//    }
//
//    @Bean
//    public AiAssistantGeminiFlashThinking aiAssistantGeminiFlashThinking(){
//        return AiServices.builder(AiAssistantGeminiFlashThinking.class)
//                .chatLanguageModel(chatLanguageModelGeminiFlashThinking())
//                .chatMemoryProvider(chatMemoryProvider())
//                .build();
//    }
//
//    /**
//     * Gemini flash lite
//     */
//    public interface AiAssistantGeminiFlashLite {
//        @SystemMessage("你只能发送表情")
//        AiMessageFormat chat(@UserMessage String message);
//    }
//    static public AiAssistantGeminiFlashLite aiAssistantGeminiFlashLite() {
//        return AiServices.builder(AiAssistantGeminiFlashLite.class)
//                .chatLanguageModel(chatLanguageModelGeminiFlashLite())
//                .chatMemoryProvider(chatMemoryProvider())
//                .build();
//    }
}
