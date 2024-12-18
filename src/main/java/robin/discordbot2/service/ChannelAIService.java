package robin.discordbot2.service;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public interface ChannelAIService {
    String aiPlayGround(MessageReceivedEvent event);

    String aiMc(MessageReceivedEvent event);
}
