package robin.discordbot2.service;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public interface ChannelAIService {

    String aiMc(MessageReceivedEvent event);

    String aiDeepseek(MessageReceivedEvent event);

    String zhaomuAI(MessageReceivedEvent event);


}
