package robin.discordbot.service;

import robin.discordbot.pojo.entity.User;

public interface ApiService {
    User getUserById(Integer id);
}
