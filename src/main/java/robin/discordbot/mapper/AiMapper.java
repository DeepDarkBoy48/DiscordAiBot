package robin.discordbot.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import robin.discordbot.pojo.entity.aiPrompt;

// 这个mapper是用来操作ai_prompts表的
@Mapper
public interface AiMapper {
    @Select("select * from ai_prompts where id = #{id};")
    aiPrompt getAiById(Integer id);

    @Delete("delete from ai_prompts where id = #{id};")
    void deleteAiById(Integer id);

    void updateAI(@Param("aiPrompt") aiPrompt aiPrompt, Integer id);

    @Insert("insert into ai_prompts(id, prompt, creator, creation_time) value (#{id}, #{prompt}, #{creator}, #{creationTime});")
    void addAi(aiPrompt aiPrompt);

}
