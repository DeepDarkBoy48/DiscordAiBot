package robin.discordbot2.service;

import robin.discordbot2.pojo.entity.aiEntity.AiMessageFormat;
import robin.discordbot2.pojo.entity.aiEntity.aiSearchFinalEntity;

public interface LangChain4jService {
    byte[] deepDarkAiHTMLFigure(String id, AiMessageFormat aiMessageFormat);

    String deepdarkaiText(String id, AiMessageFormat aiMessageFormat);

    String deepdarkaiImage(String id,AiMessageFormat aiMessageFormat);

    String deepDarkTreadAiChat(String id,AiMessageFormat aiMessageFormat);

    String embediframe(String id,AiMessageFormat aiMessageFormat);

    aiSearchFinalEntity aisearch(String id, AiMessageFormat aiMessageFormat);

    String gemini(String id, AiMessageFormat aiMessageFormat);
}
