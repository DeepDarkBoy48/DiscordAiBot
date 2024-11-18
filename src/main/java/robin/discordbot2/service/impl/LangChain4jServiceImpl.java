package robin.discordbot2.service.impl;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import dev.langchain4j.data.image.Image;
import dev.langchain4j.model.image.ImageModel;
import dev.langchain4j.model.openai.OpenAiImageModel;
import dev.langchain4j.model.output.Response;
import gui.ava.html.image.generator.HtmlImageGenerator;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import robin.discordbot2.config.Langchain4j;
import robin.discordbot2.pojo.entity.aiEntity.*;
import robin.discordbot2.service.LangChain4jService;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LangChain4jServiceImpl implements LangChain4jService {
    @Autowired
    private Langchain4j.AiAssistantFigure deepDarkAiHTMLFigure;
    @Autowired
    private Langchain4j.AiAssistantText deepDarkAiText;
    @Autowired
    private Langchain4j.AiAssistantThreadText deepDarkThreadAi;
    @Autowired
    private Langchain4j.embed embed;
    @Autowired
    private Langchain4j.aiSearchTavily aiSearchTavily;

    private final Dotenv dotenv = Dotenv.load();

    public String getOpenaiToken() {
        return dotenv.get("openaiToken");
    }

    public String getDiscordToken() {
        return dotenv.get("discordToken");
    }

    public ImageModel chatLanguageModelImage = OpenAiImageModel.builder()
            .apiKey(getOpenaiToken())
            .modelName("DALL·E 2")
            .build();

    @Override
    public byte[] deepDarkAiHTMLFigure(String id, AiMessageFormat aiMessageFormat) {
        String deepDarkResult = deepDarkAiHTMLFigure.chat(id, aiMessageFormat);
        // html2picture.htmlTransferImage(deepDarkResult);
        HtmlImageGenerator imageGenerator = new HtmlImageGenerator();
        // 加载 HTML 内容
        imageGenerator.setSize(new Dimension(800, 600));
        imageGenerator.loadHtml(deepDarkResult);
        // 保存图片到指定路径
        // imageGenerator.saveAsImage("output.png");
        // 生成 BufferedImage
        BufferedImage bufferedImage = imageGenerator.getBufferedImage();
        // 将 BufferedImage 转换为 byte[]
        // 使用 ByteArrayOutputStream 将 BufferedImage 转换为 byte[]
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(bufferedImage, "png", baos); // 将图片写入字节流，格式为 PNG
            return baos.toByteArray(); // 返回字节数组
        } catch (IOException e) {
            e.printStackTrace();
            return null; // 处理异常，返回 null
        }
    }

    @Override
    public String deepdarkaiText(String id, AiMessageFormat aiMessageFormat) {
        String deepDarkResult = deepDarkAiText.chat(id, aiMessageFormat);
        return deepDarkResult;
    }

    @Override
    public String deepdarkaiImage(String id, AiMessageFormat aiMessageFormat) {
        String message = aiMessageFormat.getMessage();
        Response<Image> response = chatLanguageModelImage.generate(message);
        return response.content().url().toString();
    }

    @Override
    public String deepDarkTreadAiChat(String id, AiMessageFormat aiMessageFormat) {
        String deepDarkResult = deepDarkThreadAi.chat(id, aiMessageFormat);
        return deepDarkResult;
    }

    @Override
    public String embediframe(String id, AiMessageFormat aiMessageFormat) {
        String embedResult = embed.chat(id, aiMessageFormat);
        return embedResult;
    }

    @Override
    public aiSearchFinalEntity aisearch(String id, AiMessageFormat aiMessageFormat) {
        Map<String, Object> requestData = new HashMap<>();
        String url = "https://api.tavily.com/search";
        String apiKey = "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";
        boolean include_images = true;
        boolean include_images_descriptions = false;
        String search_depth = "basic";
        String query = aiMessageFormat.getMessage();
        Boolean include_raw_content = true;
        // 构建请求数据
        JSONObject data = new JSONObject();
        data.put("api_key", apiKey);
        data.put("query", query);
        data.put("include_images", include_images);
        data.put("include_images_descriptions", include_images_descriptions);
        data.put("search_depth", search_depth);
        data.put("include_raw_content",include_raw_content);
        // 发送 POST 请求
        String response = HttpUtil.post(url, data.toString());
        JSONObject jsonObject = JSONUtil.parseObj(response);
        //json转为java实体类
        aiSearchEntity aiSearchEntity = jsonObject.toBean(aiSearchEntity.class);

        //ai搜索的图片
        List<String> images = aiSearchEntity.getImages();
        StringBuilder stringBuilder = new StringBuilder();
        for (String image : images){
             stringBuilder.append(image).append("\n");
        }
        String imagesInfo = stringBuilder.toString();

        //ai搜索的内容
        List<aiSearchResultEntity> results = aiSearchEntity.getResults();
        StringBuilder stringBuilder2 = new StringBuilder();
        for (aiSearchResultEntity result : results){
            stringBuilder2.append(result.toString()).append("\n");
        }
        String resultInfo = stringBuilder2.toString();

        //resultInfo交给ai重构优化
//        AiMessageFormat aiMessageFormat1 = new AiMessageFormat();
//        aiMessageFormat1.setMessage(resultInfo);
        aiSearchOutputEntity aiSearchOutputEntity = aiSearchTavily.chat(id, resultInfo);

        aiSearchFinalEntity aiSearchFinalEntity = new aiSearchFinalEntity();
        aiSearchFinalEntity.setImagesInfo(imagesInfo);
        aiSearchFinalEntity.setAiSearchOutputEntity(aiSearchOutputEntity);

        return aiSearchFinalEntity;
    }
}
