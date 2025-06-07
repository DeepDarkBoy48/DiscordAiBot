package robin.discordbot.Scheduling;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import robin.discordbot.mapper.MainChannelServiceImplTestMapper;

@Service
public class updateGeminiCount {
    @Autowired
    private MainChannelServiceImplTestMapper mainChannelServiceImplTestMapper;


    @Scheduled(cron = "0 0 0 * * ?")
    public void resetApiKeyUsageCountsDaily() {
        mainChannelServiceImplTestMapper.refreshGeminiApiKeyUsageCount();
        System.out.println("Daily API key usage counts reset.");
    }
    
}
