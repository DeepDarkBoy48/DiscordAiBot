package robin.discordbot2.utils;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;

public class JsonUtil {

    public static JSONObject readJsonFile(String filePath) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8);
        return JSON.parseObject(content);
    }

    public static void writeJsonFile(String filePath, JSONObject jsonObject) throws IOException {
        String content = JSON.toJSONString(jsonObject, String.valueOf(true));
        Files.write(Paths.get(filePath), content.getBytes(StandardCharsets.UTF_8));
    }

    public static void main(String[] args) {
        String filePath = "src/main/resources/prompts/prompts.json";
        try {
            JSONObject jsonObject = readJsonFile(filePath);
            System.out.println("原始JSON内容:\n" + jsonObject.toJSONString());

            // 修改 JSON 内容
            modifyJson(jsonObject);

            // 将修改后的 JSON 写回文件
            writeJsonFile(filePath, jsonObject);
            System.out.println("修改后的JSON内容已写入文件");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void modifyJson(JSONObject jsonObject) {
        // 示例：修改 "zhaomu" 的 system_message 的 content
        JSONObject zhaomu = jsonObject.getJSONObject("zhaomu");
        if (zhaomu != null) {
            JSONObject systemMessage = zhaomu.getJSONObject("system_message");
            if (systemMessage != null) {
                systemMessage.put("content", "你是一个修改后的助手，特点如下：\n- 自认为很聪明\n- 性格开朗、喜欢帮助别人\n...");
            }
        }

        // 示例：添加一个新的 prompt
        JSONObject newPrompt = new JSONObject();
        newPrompt.put("name", "新的助手");
        JSONObject newSystemMessage = new JSONObject();
        newSystemMessage.put("role", "system");
        newSystemMessage.put("content", "这是一个新的助手，用于测试。");
        newPrompt.put("system_message", newSystemMessage);
        jsonObject.put("new_assistant", newPrompt);
    }
}