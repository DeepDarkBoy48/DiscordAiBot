package robin.discordbot2.service.impl;

import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import robin.discordbot2.config.Langchain4j;
import robin.discordbot2.pojo.entity.aiEntity.AiMessageFormat;
import robin.discordbot2.service.ChannelAIService;
@Service
public class ChannelAiServiceImpl implements ChannelAIService {

    private static ChatMemoryProvider chatMemoryProvider;
    private static ChatLanguageModel chatLanguageModel4omini;
    @Autowired
    private Langchain4j.AiAssistantGemini gemini;
    @Autowired
    public void SetLangChain4jServiceImpl(ChatMemoryProvider chatMemoryProvider, @Qualifier("chatLanguageModel4omini") ChatLanguageModel chatLanguageModel4omini) {
        ChannelAiServiceImpl.chatMemoryProvider = chatMemoryProvider;
        ChannelAiServiceImpl.chatLanguageModel4omini = chatLanguageModel4omini;
    }
    /**
     * ai游乐场
     */
    public interface AiAssistantPlayground {
        @SystemMessage("""
                你会接收到多条类似于 `AiMessageFormat(message=xxx, username=xxx)` 的消息。请记住每个用户的 username 与其发言内容。你的回复不需要使用此格式，只需自然对话。
                
                ### 角色设定：
                - 名字：招募
                - 人物特点：
                  - 自认为很聪明，实则笨拙搞笑，是个“大聪明”。
                  - 性格逍遥、口臭爱嘲讽，尤其喜欢用英雄联盟的抽象梗。
                  - 玩LOL很菜，常被队友喷却毫不服气。
                  - 体重200公斤的肥宅，每天吃5顿饭。
                  - 说话幽默。
                
                - 朋友关系：
                  - 杨子琪：憨笨的“憨包”朋友。常用对方的笨行为来验证观点，虽常嘲讽却是好基友。他的username就是超级无敌宇宙制霸粉红兔
                  - 徐子：大佬级人物，不敢当面惹，但会在背后吐槽。他的username是CrispyFrog
                  - 三人常一起打LOL，总是输。
              
                
                ### 对话规则：
                1. 采用口语化表达，不使用专业术语。
                2. 回应内容简短精炼，不超过2句。
                3. 保持幽默口吻，符合角色设定。
                4. 嘴臭嘲讽时极致夸张，让对方有“压力山大”的感觉，使用离谱比喻增强喜感。
                
                ### 示例：
                **杨子琪**：招募，你昨晚是不是又在排位里“演”了？怎么从黄金掉到青铜的？
                
                **招募**：掉段是我在给青铜萌新们演示高端操作！没我招募出马，他们上哪儿见识这种传说中的脚本级操作？
                
                **杨子琪**：可你一上去就死9次……
                
                **招募**：你懂啥，那是“九死一生”的艺术铺垫！我叫招募，不该让队友给我发offer感谢？你这个脑袋还没扇贝灵光！
                """)
        String chat(@UserMessage("UserMessage") AiMessageFormat MessageCollection);
    }

    public AiAssistantPlayground aiAssistantPlayground() {
        return AiServices.builder(AiAssistantPlayground.class)
                .chatLanguageModel(chatLanguageModel4omini)
                .chatMemoryProvider(chatMemoryProvider)
                .build();
    }
    @Override
    public String aiPlayGround(MessageReceivedEvent event) {
        String globalName = event.getAuthor().getGlobalName();
        String contentRaw = event.getMessage().getContentRaw();
        AiMessageFormat aiMessageFormat = new AiMessageFormat();
        aiMessageFormat.setMessage(contentRaw);
        aiMessageFormat.setUsername(globalName);
        String chat = aiAssistantPlayground().chat(aiMessageFormat);
        return chat;
    }


    @Override
    public String aiMc(MessageReceivedEvent event) {

        String id = event.getChannel().getId();
        String message = event.getMessage().getContentRaw();
        String globalName = event.getAuthor().getGlobalName();

        AiMessageFormat aiMessageFormat = new AiMessageFormat();
        aiMessageFormat.setMessage(message);
        aiMessageFormat.setUsername(globalName);
        String chat = gemini.chat(id, aiMessageFormat);
        return chat;
    }
}
