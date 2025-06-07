package robin.discordbot.service.impl;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import cn.hutool.json.JSONUtil;
import dev.langchain4j.agent.tool.*;
import dev.langchain4j.mcp.McpToolProvider;
import dev.langchain4j.mcp.client.DefaultMcpClient;
import dev.langchain4j.mcp.client.McpClient;
import dev.langchain4j.mcp.client.transport.McpTransport;
import dev.langchain4j.mcp.client.transport.stdio.StdioMcpTransport;
import dev.langchain4j.service.tool.*;
import jakarta.annotation.PostConstruct;
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
import robin.discordbot.mapper.MainChannelServiceImplTestMapper;
import robin.discordbot.pojo.entity.aiPrompt;
import robin.discordbot.pojo.entity.gemini_api_key_entity;
import robin.discordbot.service.MainChannelAIService;
import robin.discordbot.record.llmModel;
import robin.discordbot.utils.GeminiFactory;

@Service
public class MainChannelAIServiceImplTest implements MainChannelAIService {

    @Autowired
    private AiMapper aiMapper;

    @Autowired
    private MainChannelServiceImplTestMapper mainChannelServiceImplTestMapper;

    private Dotenv dotenv = Dotenv.load();

    // 添加成员变量用于存储和轮换 API 密钥
    private List<String> geminiApiKeys;
    private int currentKeyIndex = 0;
    private int keyUsageCount = 0;
    private static final int MAX_KEY_USAGE = 1; // 每个 key 使用的最大次数

    // 使用实例初始化块加载 API 密钥
    @PostConstruct
    public void initializer() {
        geminiApiKeys = new ArrayList<>();
        List<gemini_api_key_entity> geminiApiKeysEntity = mainChannelServiceImplTestMapper.getAllGeminiApiKeys();
        for (gemini_api_key_entity geminiApiKeyEntity : geminiApiKeysEntity) {
            geminiApiKeys.add(geminiApiKeyEntity.getApiKey());
        }
    }


    //  创建一个全局共享的 ChatMemory 实例
    private final ChatMemory chatMemory = MessageWindowChatMemory.builder().maxMessages(100).build();

    // 修改 getGeminiToken 方法以实现轮换逻辑
    private synchronized String getGeminiToken() { // 添加 synchronized 保证线程安全
        if (geminiApiKeys == null || geminiApiKeys.isEmpty()) {
            // 处理未加载密钥的情况
            System.err.println("错误：Gemini API 密钥列表为空或未初始化。");
            // 根据需要返回 null、抛出异常或返回默认/占位符密钥
            // return "DEFAULT_OR_PLACEHOLDER_KEY";
            throw new IllegalStateException("Gemini API 密钥未加载。");
        }

        // 获取当前 key
        String currentKey = geminiApiKeys.get(currentKeyIndex);
        keyUsageCount++; // 增加使用计数
        System.out.println("使用 API Key 索引: " + currentKeyIndex + ", 使用次数: " + keyUsageCount + "/" + MAX_KEY_USAGE); // 调试日志

        // 检查是否需要切换到下一个 key
        if (keyUsageCount >= MAX_KEY_USAGE) {
            currentKeyIndex = (currentKeyIndex + 1) % geminiApiKeys.size(); // 循环切换索引
            keyUsageCount = 0; // 重置新 key 的使用计数
            System.out.println("切换到下一个 API Key 索引: " + currentKeyIndex); // 调试日志
        }

        return currentKey; // 返回当前使用的 key
    }

    interface AiPlayGroundTest {
        String chat(@UserMessage String message) throws NoSuchMethodException;
    }


    @Override
    public String aiPlayGroundTest(MessageReceivedEvent event) throws NoSuchMethodException {
        String contentRaw = event.getMessage().getContentRaw();
        String systemMessage = "";
        String modelName = "";

        //过五关,斩六将
        // 1. 获取系统提示词
        // 2. 获取模型名称
        // 3. 获取AI实体
        // 4. 如果AI实体不存在,则创建一个AI实体，并设置模型名称和系统提示词
        // 5. 如果AI实体存在,则更新AI实体
        // 6. 如果AI实体的模型名称不存在,则创建一个AI实体，并设置模型名称
        // 7. 如果AI实体的系统提示词不存在,则创建一个AI实体，并设置系统提示词

        aiPrompt aiEntity = aiMapper.getAiById(1);

        if (aiEntity.getModelName().isEmpty() || aiEntity.getPrompt().isEmpty()) {
            aiPrompt aiPrompt = new aiPrompt();
            aiPrompt.setModelName("gemini-2.0-flash");
            aiPrompt.setPrompt("Hello, I am Gemini. How can I help you?");
            aiPrompt.setCreationTime(LocalDateTime.now());
            aiMapper.updateAI(aiPrompt, 1);
            aiEntity = aiMapper.getAiById(1);
        }
        modelName = aiEntity.getModelName();
        systemMessage = aiEntity.getPrompt();

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
        }

        // 获取 Gemini API 密钥 轮询
        String apiKey = GeminiFactory.getGeminiToken();

        // 创建 Gemini 模型实例
        ChatLanguageModel model = GoogleAiGeminiChatModel.builder()
                .apiKey(apiKey) // 需要替换为你的 Gemini API 密钥 [2]
                .modelName(modelName) //  模型名称 [2]
                .build();

        // 创建 MCP 传输实例
        McpTransport transport = new StdioMcpTransport.Builder()
                .command(List.of("/Users/robinmacmini/.nvm/versions/node/v23.5.0/bin/npx",
                        "npx",
                        "-y",
                        "tavily-mcp@0.1.4"
                ))
                .environment(Map.of("TAVILY_API_KEY", "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"))
                .logEvents(true)
                .build();
        McpClient mcpClient = new DefaultMcpClient.Builder()
                .transport(transport)
                .build();
        ToolProvider toolProvider = McpToolProvider.builder()
                .mcpClients(List.of(mcpClient))
                .build();


          //todo  // 创建low level工具
        //define the toolSpecifications
        List<ToolSpecification> toolSpecifications = ToolSpecifications.toolSpecificationsFrom(toolbox.class);
        // define the ToolExecutor
        List<ToolExecutor> toolExecutors = new ArrayList<>();
        toolExecutors.add((ToolExecutionRequest toolExecutionRequest, Object memoryId) -> {
            Map<String, Object> arguments = JSONUtil.parseObj(toolExecutionRequest.arguments());
            return new toolbox().getForecast(arguments.get("location").toString()).toString();
        });
        toolExecutors.add((ToolExecutionRequest toolExecutionRequest, Object memoryId) -> {
            Map<String, Object> arguments = JSONUtil.parseObj(toolExecutionRequest.arguments());
            return new toolbox().getGeminiApiKeyUsageCount().toString();
        });
        toolProvider = (ToolProviderRequest) -> {
            return ToolProviderResult.builder()
                    .add(toolSpecifications.get(0), toolExecutors.get(0))
                    .add(toolSpecifications.get(1), toolExecutors.get(1))
                    .build();
        };

        String finalSystemMessage = systemMessage;
        System.out.println("finalSystemMessage: " + finalSystemMessage);
        System.out.println("modelName: " + modelName);
        AiPlayGroundTest aiPlayGroundTest = AiServices.builder(AiPlayGroundTest.class)
                .chatMemory(chatMemory)
                .chatLanguageModel(model)
                .systemMessageProvider(id -> finalSystemMessage)
//                .tools(new toolbox())
                .toolProvider(toolProvider)
                .build();
        System.out.println("contentRaw: " + contentRaw);

        try {
            String chat = aiPlayGroundTest.chat(contentRaw);
            mainChannelServiceImplTestMapper.updateGeminiApiKeyUsageCount(apiKey, LocalDateTime.now());
            return chat;
        } catch (Exception e) {
            System.out.println("error: " + e.getMessage());
            currentKeyIndex = (currentKeyIndex + 1) % geminiApiKeys.size(); // 循环切换索引
            return "error: " + e.getMessage();
        } finally {
        }
    }

    class toolbox {
        public record WeatherForecast(
                String location,
                String forecast,
                int temperature) {
        }
//        @Tool("Get the weather forecast for a location")
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


//        @Tool("Get the gemini api key usage count")
        String getGeminiApiKeyUsageCount() {
            TreeMap<String, Integer> treeMap = new TreeMap<>();
            String[] geminiAccounts = new String[4];
            geminiAccounts[0] = "zuiquan200818@gmail.com";
            geminiAccounts[1] = "warghost200818@gmail.com";
            geminiAccounts[2] = "xuchenyang36robin@gmail.com";
            geminiAccounts[3] = "essnoto605@gmail.com";
            Integer totalCount = 0;
            List<gemini_api_key_entity> allGeminiApiKeys = mainChannelServiceImplTestMapper.getAllGeminiApiKeys();
            int i = 0;
            for(gemini_api_key_entity geminiApiKeyEntity : allGeminiApiKeys) {
                treeMap.put(geminiAccounts[i], geminiApiKeyEntity.getUsageCount());
                totalCount += geminiApiKeyEntity.getUsageCount();
                i++;
            }
            String usage = treeMap.toString();
            usage = "每个账号剩余使用的次数: " + usage + "\n" + "总剩余次数: " + totalCount;
            return usage;
        }

    }

    @Override
    public String aiPlayGround(MessageReceivedEvent event) {
        return "try another channel";
    }

}
