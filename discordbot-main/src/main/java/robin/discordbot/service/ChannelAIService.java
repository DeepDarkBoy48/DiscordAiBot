package robin.discordbot.service;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public interface ChannelAIService {


    String aiDeepseek(MessageReceivedEvent event);

    String zhaomuAI(MessageReceivedEvent event);


}
