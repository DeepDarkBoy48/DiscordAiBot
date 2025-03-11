package robin.discordbot.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import robin.discordbot.pojo.entity.User;

@Mapper
public interface UserMapper {
    @Select("select * from users where id = #{id};")
    User getUserById(Integer id);
}
