package robin.discordbot2.listener;

import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import jakarta.annotation.Resource;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import robin.discordbot2.config.Langchain4j;
import robin.discordbot2.config.RegularConfig;
import robin.discordbot2.pojo.entity.aiEntity.AiMessageFormat;
import robin.discordbot2.service.ChannelAIService;
import robin.discordbot2.service.LangChain4jService;
import robin.discordbot2.utils.discordWordsLimit;

import java.util.Stack;

@Service
public class ChannelListener extends ListenerAdapter {


    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) {
            return;
        }
        if (event.getChannel().getName().equals("ai游乐园")) {
            ChannelAIService channelAIService = RegularConfig.getChannelAIService();
            String result = channelAIService.aiPlayGround(event);
            discordWordsLimit.splitParagraph(result, event);
        } else if (event.getChannel().getName().equals("mc百科")) {
            ChannelAIService channelAIService = RegularConfig.getChannelAIService();
            String result = channelAIService.aiMc(event);
            discordWordsLimit.splitParagraph(result, event);
        }
    }
}
