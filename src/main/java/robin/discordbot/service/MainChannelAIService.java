package robin.discordbot.service;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public interface MainChannelAIService {

    String aiPlayGroundAGENT(MessageReceivedEvent event);

    String aiPlayGroundMCP(MessageReceivedEvent event) throws Exception;
}
