package robin.discordbot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import robin.discordbot.service.ChannelAIService;
import robin.discordbot.service.LangChain4jService;
import robin.discordbot.service.MainChannelAIService;

@Component
public class RegularConfig {

    private static LangChain4jService langChain4jService;
    private static ChannelAIService channelAIService;
    private static MainChannelAIService mainChannelAIService;
    private static MainChannelAIService mainChannelAIServiceImplTest;
    @Autowired
    public void SetRegularConfig(LangChain4jService langChain4jService, ChannelAIService channelAIService, @Qualifier("mainChannelAIServiceImpl") MainChannelAIService mainChannelAIService, @Qualifier("mainChannelAIServiceImplTest") MainChannelAIService mainChannelAIServiceImplTest ){
        RegularConfig.langChain4jService = langChain4jService;
        RegularConfig.channelAIService = channelAIService;
        RegularConfig.mainChannelAIService = mainChannelAIService;
        RegularConfig.mainChannelAIServiceImplTest = mainChannelAIServiceImplTest;
    }

    public static LangChain4jService getLangchain4jservice(){
        return  langChain4jService;
    }

    public static ChannelAIService getChannelAIService(){
        return channelAIService;
    }

    public static MainChannelAIService getMainChannelAIService(){
        return mainChannelAIService;
    }

    public static MainChannelAIService getMainChannelAIServiceImplTest(){
        return mainChannelAIServiceImplTest;
    }

}
