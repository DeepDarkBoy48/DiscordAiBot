package robin.discordbot.pojo;

import lombok.Data;
import robin.discordbot.pojo.entity.User;

import java.util.List;


@Data
public class UserGroup {
    private String id;
    private List<User> userList;

}
