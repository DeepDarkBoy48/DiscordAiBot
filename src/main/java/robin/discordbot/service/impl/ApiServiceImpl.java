package robin.discordbot.service.impl;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import robin.discordbot.mapper.UserMapper;
import robin.discordbot.pojo.entity.User;
import robin.discordbot.service.ApiService;

@Service
public class ApiServiceImpl implements ApiService {
    @Resource
    private UserMapper userMapper;
    @Override
    public User getUserById(Integer id) {
        User user = userMapper.getUserById(id);
        return user;
    }
}
