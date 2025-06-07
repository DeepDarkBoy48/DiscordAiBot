package robin.discordbot.service;

import robin.discordbot.apiPojo.dto.translateResult;
import robin.discordbot.pojo.entity.User;

public interface ApiService {
    User getUserById(Integer id);

    translateResult getTranslate(String text);
}
