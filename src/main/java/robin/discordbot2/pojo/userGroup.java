package robin.discordbot2.pojo;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;


@Data
public class userGroup {
    private String id;
    private List<User> userList;


    class User {
        private String username;
        private String password;
        private String email;
        private String createTime;
        private String updateTime;
    }
}
