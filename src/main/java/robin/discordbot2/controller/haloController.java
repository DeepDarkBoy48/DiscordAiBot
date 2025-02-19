package robin.discordbot2.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;
import robin.discordbot2.pojo.entity.Result;
import robin.discordbot2.pojo.entity.User;
import robin.discordbot2.service.HaloService;

@RestController
@RequestMapping("/helloworld")
public class haloController {

    @Resource
    private HaloService haloService;
    @GetMapping("/aichat/{id}")
    public String hello(@PathVariable("id") Integer id){
        User user = haloService.getUserById(id);
        String userInfo = user.toString();
        return userInfo;
    }

    @GetMapping("/test")
    public Result test(){
        return Result.success();
    }

    @PostMapping("/articles")
    public String article(@RequestParam String article){
        System.out.println(article);
        return "success";
    }
}
