package robin.discordbot.controller;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.dashscope.QwenChatModel;
import io.github.cdimascio.dotenv.Dotenv;

import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import robin.discordbot.pojo.UserGroup;
import robin.discordbot.pojo.entity.Result;
import robin.discordbot.pojo.entity.User;
import robin.discordbot.pojo.entity.aiEntity.AiMessageFormat;
import robin.discordbot.service.ApiService;
import robin.discordbot.utils.entityTest;

@RestController
@RequestMapping("/api")
public class ApiController {

    /**
     * Apifox Helper Dify controller
     */
    @Resource
    private ApiService haloService;
    public static Dotenv dotenv = Dotenv.load();
    public static String getQwenToken() {
        return dotenv.get("qwen");
    }

    @GetMapping("/aichat/{id}")
    public String hello(@PathVariable("id") Integer id){
        User user = haloService.getUserById(id);
        String userInfo = user.toString();
        return userInfo;
    }

    @GetMapping("/test")
    public Result test(){
        ChatLanguageModel qwenChatModel = QwenChatModel.builder()
                .apiKey(getQwenToken())
                .modelName("deepseek-r1")
                .build();
        String generate = qwenChatModel.generate("你好，你叫什么名字？");
        AiMessageFormat aiMessageFormat = new AiMessageFormat();
        aiMessageFormat.setMessage(generate);
        aiMessageFormat.setUsername("机器人");
        UserGroup sampleUserGroup = entityTest.createSampleUserGroup();
        return Result.success(sampleUserGroup);
    }

    @PostMapping("/articles")
    public String article(@RequestParam String article){
        System.out.println(article);
        return "success";
    }
}
