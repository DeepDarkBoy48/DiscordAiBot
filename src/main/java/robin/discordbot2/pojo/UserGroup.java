package robin.discordbot2.pojo;

import lombok.Data;
import robin.discordbot2.pojo.entity.User;

import java.util.List;


@Data
public class UserGroup {
    private String id;
    private List<User> userList;

}
