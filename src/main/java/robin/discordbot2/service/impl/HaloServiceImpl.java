package robin.discordbot2.service.impl;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import robin.discordbot2.mapper.UserMapper;
import robin.discordbot2.pojo.entity.User;
import robin.discordbot2.service.HaloService;

@Service
public class HaloServiceImpl implements HaloService {
    @Resource
    private UserMapper userMapper;
    @Override
    public User getUserById(Integer id) {
        User user = userMapper.getUserById(id);
        return user;
    }
}
