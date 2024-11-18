package robin.discordbot2.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import robin.discordbot2.service.LangChain4jService;

@Component
public class RegularConfig {
    private static LangChain4jService langChain4jService;
    @Autowired
    public void SetRegularConfig(LangChain4jService langChain4jService){
        RegularConfig.langChain4jService = langChain4jService;
    }

    public static LangChain4jService getLangchain4jservice(){
        return  langChain4jService;
    }

}
