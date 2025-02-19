package robin.discordbot2.service;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public interface MainChannelAIService {

    String aiPlayGround(MessageReceivedEvent event);

    String aiPlayGroundTest(MessageReceivedEvent event) throws NoSuchMethodException;
}
