package robin.discordbot2.service;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import robin.discordbot2.pojo.entity.aiEntity.AiMessageFormat;
import robin.discordbot2.pojo.entity.aiEntity.aiSearchFinalEntity;

public interface LangChain4jService {
    byte[] deepDarkAiHTMLFigure(String id, AiMessageFormat aiMessageFormat);

    String deepdarkaiText(String id, AiMessageFormat aiMessageFormat);

    String deepdarkaiImage(String id,AiMessageFormat aiMessageFormat);

    String deepDarkTreadAiChat(String id,AiMessageFormat aiMessageFormat);

    String embediframe(String id,AiMessageFormat aiMessageFormat);

    aiSearchFinalEntity aisearch(String id, AiMessageFormat aiMessageFormat);

    String grok(String message);

    String gemini(String id, AiMessageFormat aiMessageFormat);

    aiSearchFinalEntity aisearchNSFW(String id, AiMessageFormat aiMessageFormat);


}
