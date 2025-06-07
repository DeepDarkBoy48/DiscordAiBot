package robin.discordbot.service.impl;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import robin.discordbot.apiPojo.dto.translateResult;
import robin.discordbot.mapper.AiMapper;
import robin.discordbot.mapper.UserMapper;
import robin.discordbot.pojo.entity.User;
import robin.discordbot.service.ApiService;
import robin.discordbot.utils.GeminiFactory;

import java.util.function.Function;

@Service
public class ApiServiceImpl implements ApiService {
    @Resource
    private UserMapper userMapper;

    @Resource
    private AiMapper aiMapper;

    @Override
    public User getUserById(Integer id) {
        User user = userMapper.getUserById(id);
        return user;
    }

    @Override
    public translateResult getTranslate(String text) {

        // 获取 Gemini API 密钥 轮询
        String apiKey = GeminiFactory.getGeminiToken();

        ChatLanguageModel model = GoogleAiGeminiChatModel.builder()
                .apiKey(apiKey)
                .modelName("gemini-2.0-flash-lite")
                .build();
        Translator translator = AiServices.builder(Translator.class)
                .chatLanguageModel(model)
                .systemMessageProvider(new Function<Object, String>() {
                    @Override
                    public String apply(Object o) {
                        return "if you receive Chinese text then translate it to English. On the contrary, if you receive English text then translate it to Chinese. Only output the translation. Dont' response additional information";
                    }
                })
                .build();
        String result = "";
        try {
            result = translator.chat("translate:" + text);
            translateResult translate = new translateResult();
            translate.setResult(result);
            return translate;
        } catch (Exception e) {
            System.out.println("error: " + e.getMessage());
            return null;
        }
    }


    interface Translator {
        String chat(@UserMessage String message) throws NoSuchMethodException;
    }
}
