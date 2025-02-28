package robin.discordbot2.utils;


import robin.discordbot2.pojo.UserGroup;
import robin.discordbot2.pojo.entity.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class entityTest {
    public static UserGroup createSampleUserGroup() {
        UserGroup userGroup = new UserGroup();
        userGroup.setId("group1");

        List<User> users = new ArrayList<>();
        users.add(new User("user1", "password1", "user1@example.com", LocalDateTime.now(), LocalDateTime.now()));
        users.add(new User("user2", "password2", "user2@example.com", LocalDateTime.now(), LocalDateTime.now()));
        users.add(new User("user3", "password3", "user3@example.com", LocalDateTime.now(), LocalDateTime.now()));

        userGroup.setUserList(users);
        return userGroup;
    }
}
