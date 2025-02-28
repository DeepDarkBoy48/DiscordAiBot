package robin.discordbot2.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import robin.discordbot2.pojo.entity.User;

@Mapper
public interface UserMapper {
    @Select("select * from users where id = #{id};")
    User getUserById(Integer id);
}
