package robin.discordbot.controller;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.dashscope.QwenChatModel;
import io.github.cdimascio.dotenv.Dotenv;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import robin.discordbot.pojo.UserGroup;
import robin.discordbot.pojo.entity.Result;
import robin.discordbot.pojo.entity.User;
import robin.discordbot.pojo.entity.aiEntity.AiMessageFormat;
import robin.discordbot.service.ApiService;
import robin.discordbot.service.LangChain4jService;
import robin.discordbot.utils.entityTest;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final ApiService haloService;
    private final LangChain4jService langChain4jService;

    public static Dotenv dotenv = Dotenv.load();

    @Autowired
    public ApiController(ApiService haloService, LangChain4jService langChain4jService) {
        this.haloService = haloService;
        this.langChain4jService = langChain4jService;
    }

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

    @PostMapping("/chat")
    public String chatWithBot(@RequestBody String message) {
        return langChain4jService.chat(message);
    }
}
