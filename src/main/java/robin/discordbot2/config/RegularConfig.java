package robin.discordbot2.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import robin.discordbot2.service.ChannelAIService;
import robin.discordbot2.service.LangChain4jService;

@Component
public class RegularConfig {

    private static LangChain4jService langChain4jService;
    private static ChannelAIService channelAIService;
    @Autowired
    public void SetRegularConfig(LangChain4jService langChain4jService, ChannelAIService channelAIService){
        RegularConfig.langChain4jService = langChain4jService;
        RegularConfig.channelAIService = channelAIService;
    }

    public static LangChain4jService getLangchain4jservice(){
        return  langChain4jService;
    }

    public static ChannelAIService getChannelAIService(){
        return channelAIService;
    }

}
