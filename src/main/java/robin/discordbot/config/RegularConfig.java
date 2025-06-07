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
    private static MainChannelAIService mainChannelAIServiceImplAGENT;
    private static MainChannelAIService mainChannelAIServiceImplMCP;

    @Autowired
    public void SetRegularConfig(LangChain4jService langChain4jService, ChannelAIService channelAIService, @Qualifier("mainChannelAIServiceImplAGENT") MainChannelAIService mainChannelAIServiceImplAGENT, @Qualifier("mainChannelAIServiceImplMCP") MainChannelAIService mainChannelAIServiceImplMCP) {
        RegularConfig.langChain4jService = langChain4jService;
        RegularConfig.channelAIService = channelAIService;
        RegularConfig.mainChannelAIServiceImplAGENT = mainChannelAIServiceImplAGENT;
        RegularConfig.mainChannelAIServiceImplMCP = mainChannelAIServiceImplMCP;
    }

    public static LangChain4jService getLangchain4jservice() {
        return langChain4jService;
    }

    public static ChannelAIService getChannelAIService() {
        return channelAIService;
    }

    public static MainChannelAIService getMainChannelAIService() {
        return mainChannelAIServiceImplAGENT;
    }

    public static MainChannelAIService getMainChannelAIServiceImplMCP() {
        return mainChannelAIServiceImplMCP;
    }

}
