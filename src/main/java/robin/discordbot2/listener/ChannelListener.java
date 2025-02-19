package robin.discordbot2.listener;

import org.springframework.stereotype.Service;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import robin.discordbot2.config.RegularConfig;
import robin.discordbot2.service.ChannelAIService;
import robin.discordbot2.service.MainChannelAIService;
import robin.discordbot2.utils.discordWordsLimit;

@Service
public class ChannelListener extends ListenerAdapter {


    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) {
            return;
        }
        String result = null;
        if (event.getChannel().getName().equals("ai游乐园")) {
            MainChannelAIService mainChannelAIService = RegularConfig.getMainChannelAIService();
             result = mainChannelAIService.aiPlayGround(event);
        } else if (event.getChannel().getName().equals("mc百科")) {
            ChannelAIService channelAIService = RegularConfig.getChannelAIService();
             result = channelAIService.aiMc(event);
        }else if (event.getChannel().getName().equals("deepseek")){
            ChannelAIService channelAIService = RegularConfig.getChannelAIService();
             result = channelAIService.aiDeepseek(event);
        } else if (event.getChannel().getName().equals("招募ai")) {
            ChannelAIService channelAIService = RegularConfig.getChannelAIService();
             result = channelAIService.zhaomuAI(event);
        } else if (event.getChannel().getName().equals("ai-测试")) {
            MainChannelAIService mainChannelAIService = RegularConfig.getMainChannelAIServiceImplTest();
            try {
                result = mainChannelAIService.aiPlayGroundTest(event);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
        discordWordsLimit.splitParagraph(result, event);
    }
}
