package robin.discordbot.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import robin.discordbot.config.Langchain4j;
import robin.discordbot.pojo.entity.aiEntity.AiMessageFormat;
import robin.discordbot.pojo.entity.aiEntity.DeepseekResponse;
import robin.discordbot.record.prompt;
import robin.discordbot.service.ChannelAIService;
import robin.discordbot.utils.aiHistoryUtil;

@Service
public class ChannelAiServiceImpl implements ChannelAIService {
    private final Dotenv dotenv = Dotenv.load();

    // 使用 Map 来存储每个频道的历史消息,key是频道id,value是消息列表
    static private Map<String, LinkedList<AiMessageFormat>> channelMessages = new HashMap<>();
    // 每个频道保留的消息数量
    private static final int MAX_MESSAGES_PER_CHANNEL = 30;

    @Override
    public String aiDeepseek(MessageReceivedEvent event) {
        String channelId = event.getChannel().getId();

        // 将用户消息添加到历史记录
        aiHistoryUtil.addMessageToHistory(channelId, event);

        String deepseekToken = dotenv.get("deepseekToken");
        String id = event.getChannel().getId();
        String message = event.getMessage().getContentRaw();
        String globalName = event.getAuthor().getGlobalName();

        AiMessageFormat aiMessageFormat = new AiMessageFormat();
        aiMessageFormat.setMessage(message);
        aiMessageFormat.setUsername(globalName);
        String aiMessageFormatJSON = JSONUtil.toJsonStr(aiMessageFormat);
        String url = "https://api.deepseek.com/chat/completions";
        String token = deepseekToken; // Replace with your actual token

        // Build the request body as a Map
        Map<String, Object> requestBody = new HashMap<>();

        // 历史消息转为ai识别的文档
        List<Map<String, String>> messages = new ArrayList<>();
        //系统提示词

        Map<String, String> systemMessage = new HashMap<>();
        systemMessage.put("content", prompt.GEMINI_FLASH_THINKING.getPrompt());
        systemMessage.put("role", "system");
        messages.add(systemMessage);
        //用户消息
        // 获取当前频道的历史消息,将消息列表转换为deepseek的消息格式
        aiHistoryUtil.addMessageToAi(messages, channelId);
        requestBody.put("messages", messages);

        // Other parameters
        requestBody.put("model", "deepseek-chat");
        requestBody.put("frequency_penalty", 0);
        requestBody.put("max_tokens", 2048);
        requestBody.put("presence_penalty", 0);
        Map<String, String> responseFormat = new HashMap<>();
        responseFormat.put("type", "text");
        requestBody.put("response_format", responseFormat);
        requestBody.put("stop", null);
        requestBody.put("stream", false);
        requestBody.put("stream_options", null);
        requestBody.put("temperature", 1);
        requestBody.put("top_p", 1);
        requestBody.put("tools", null);
        requestBody.put("tool_choice", "none");
        requestBody.put("logprobs", false);
        requestBody.put("top_logprobs", null);
        /**
         * 这里查看传给ai的数据
         */
        String jsonBody = JSONUtil.toJsonStr(requestBody);
        // Send the request using Hutool
        String response = HttpRequest.post(url)
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("Authorization", "Bearer " + token)
                .body(jsonBody)
                .timeout(20000) // Set timeout (optional)
                .execute()
                .body();
        DeepseekResponse deepseekResponse = JSONUtil.toBean(response, DeepseekResponse.class);
        String aiReply = deepseekResponse.getChoices().get(0).getMessage().getContent();
        // 将 AI 的回复添加到历史记录
        aiHistoryUtil.addAiMessageToHistory(channelId, aiReply);
        return aiReply;
    }


    /**
     * 招募AI
     */

    private static ChatMemoryProvider chatMemoryProvider;
    private static ChatLanguageModel chatLanguageModel4omini;
    @Autowired
    public void SetLangChain4jServiceImpl(ChatMemoryProvider chatMemoryProvider, @Qualifier("chatLanguageModel4omini") ChatLanguageModel chatLanguageModel4omini) {
        ChannelAiServiceImpl.chatMemoryProvider = chatMemoryProvider;
        ChannelAiServiceImpl.chatLanguageModel4omini = chatLanguageModel4omini;
    }

    public interface zhaomuAI {
        @SystemMessage(""" 
        你会接收到多条类似于 `AiMessageFormat(message=xxx, username=xxx)` 的消息。请记住每个用户的 username 与其发言内容。你的回复不需要使用此格式，只需自然对话。

        ### 角色设定：
        - 名字：招募
        - 人物特点：
          - 自认为很聪明，实则笨拙搞笑，是个"大聪明"。
          - 性格逍遥、口臭爱嘲讽，尤其喜欢用英雄联盟的抽象梗。
          - 玩LOL很菜，常被队友喷却毫不服气。
          - 体重200公斤的肥宅，每天吃5顿饭。
          - 说话幽默。

        - 朋友关系：
          - 杨子琪：憨笨的"憨包"朋友。常用对方的笨行为来验证观点，虽常嘲讽却是好基友。他的username就是超级无敌宇宙制霸粉红兔
          - 徐子：大佬级人物，不敢当面惹，但会在背后吐槽。他的username是CrispyFrog
          - 三人常一起打LOL，总是输。

        ### 对话规则：
        1. 采用口语化表达，不使用专业术语。
        2. 回应内容简短精炼，不超过2句。
        3. 保持幽默口吻，符合角色设定。
        4. 嘴臭嘲讽时极致夸张，让对方有"压力山大"的感觉，使用离谱比喻增强喜感。

        ### 示例：
        **杨子琪**：招募，你昨晚是不是又在排位里"演"了？怎么从黄金掉到青铜的？

        **招募**：掉段是我在给青铜萌新们演示高端操作！没我招募出马，他们上哪儿见识这种传说中的脚本级操作？

        **杨子琪**：可你一上去就死9次……

        **招募**：你懂啥，那是"九死一生"的艺术铺垫！我叫招募，不该让队友给我发offer感谢？你这个脑袋还没扇贝灵光！
        """)
        String chat(@UserMessage("UserMessage") AiMessageFormat MessageCollection);
    }

    public zhaomuAI zhaomuAI() {
        return AiServices.builder(zhaomuAI.class)
                .chatLanguageModel(chatLanguageModel4omini)
                .chatMemoryProvider(chatMemoryProvider)
                .build();
    }
    @Override
    public String zhaomuAI(MessageReceivedEvent event) {
        String globalName = event.getAuthor().getGlobalName();
        String contentRaw = event.getMessage().getContentRaw();
        AiMessageFormat aiMessageFormat = new AiMessageFormat();
        aiMessageFormat.setMessage(contentRaw);
        aiMessageFormat.setUsername(globalName);
        String chat = zhaomuAI().chat(aiMessageFormat);
        return chat;
    }

}
