package robin.discordbot2.listener;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.concrete.ThreadChannel;
import net.dv8tion.jda.api.entities.channel.forums.ForumTag;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.user.update.UserUpdateOnlineStatusEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import robin.discordbot2.config.RegularConfig;
import robin.discordbot2.pojo.entity.aiEntity.AiMessageFormat;
import robin.discordbot2.pojo.entity.aiEntity.aiSearchFinalEntity;
import robin.discordbot2.service.LangChain4jService;
import robin.discordbot2.utils.discordWordsLimit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Service
public class MyListener extends ListenerAdapter {
    private String globalName = "";
    private Integer messageCount = 0;

    // 发消息的事件
    /**
     * Event fires when a message is sent in discord.
     * Warning: Will require "Guild Messages" gateway intent after August 2022!
     */
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        // After August 2022 will require gateway intent
        if (event.isFromThread()
                && event.getChannel().asThreadChannel().getParentChannel().getName().equals("ai聊天")) {
            // 忽略机器人消息
            // 如果是bot的消息1263657178540019763，就不回复
            if (event.getMessage().getAuthor().getId().equals("1263657178540019763")) {
                // System.out.println(event.getMessage().getAuthor().getId());
                return;
            }
            // 获取应用的标签
            List<ForumTag> appliedTags = event.getChannel().asThreadChannel().getAppliedTags();
            String result = "";
            for (ForumTag forumTag : appliedTags) {
                if (forumTag.getName().equals("chatgpt-4o-mini")) {
                    result = chatgpt4omini(event);
                }
                if (forumTag.getName().equals("embed")) {
                    result = embed(event);
                }
                if (forumTag.getName().equals("aisearchNSFW")) {
                    result = aisearch(event);
                }
                if (forumTag.getName().equals("aisearch")) {
                    result = grok(event);
                }
                if (forumTag.getName().equals("grok")) {
                    result = grok(event);
                }
                if (forumTag.getName().equals("gemini")) {
                    result = gemini(event);
                }
            }
            discordWordsLimit.splitParagraph(result, event);
        }
    }

    public String chatgpt4omini(MessageReceivedEvent event){
        ThreadChannel post = event.getChannel().asThreadChannel();
        // 获取频道id
        String postId = post.getId();
        // 获取消息
        String message = event.getMessage().getContentRaw();
        AiMessageFormat aiMessageFormat = new AiMessageFormat();
        aiMessageFormat.setMessage(message);
        LangChain4jService langchain4jservice = RegularConfig.getLangchain4jservice();
        String deepDarkResult = langchain4jservice.deepDarkTreadAiChat(postId, aiMessageFormat);
        return deepDarkResult;
    }

    public String embed(MessageReceivedEvent event) {
        String message = event.getMessage().getContentRaw();
        LangChain4jService langchain4jservice = RegularConfig.getLangchain4jservice();
        String id = event.getChannel().asThreadChannel().getParentChannel().getId();
        AiMessageFormat aiMessageFormat = new AiMessageFormat();
        aiMessageFormat.setMessage(message);
        String embedResult = langchain4jservice.embediframe(id, aiMessageFormat);
        return embedResult;
    }

    public String aisearch(MessageReceivedEvent event) {
        String message = event.getMessage().getContentRaw();
        LangChain4jService langchain4jservice = RegularConfig.getLangchain4jservice();
        String id = event.getChannel().asThreadChannel().getParentChannel().getId();
        AiMessageFormat aiMessageFormat = new AiMessageFormat();
        aiMessageFormat.setMessage(message);
        // aichat
        aiSearchFinalEntity aisearchResult = langchain4jservice.aisearch(id, aiMessageFormat);

        // 创建带按钮的搜索结果
        createSearchButton(event, aisearchResult);

        return "success";
    }

    public void createSearchButton(MessageReceivedEvent event, aiSearchFinalEntity aiSearchFinalEntity) {
        List<String> buttonInfo = aiSearchFinalEntity.getAiSearchOutputEntity().getButtonInfo();
        // 创建按钮
        Button option1Button = Button.primary("option1", buttonInfo.get(0));
        Button option2Button = Button.success("option2", buttonInfo.get(1));
        Button option3Button = Button.danger("option3", buttonInfo.get(2));
        Button cancelButton = Button.danger("option4", "cancel");

        ActionRow actionRow = ActionRow.of(option1Button, option2Button, option3Button, cancelButton);
        // 图片
        String imagesInfo = aiSearchFinalEntity.getImagesInfo();
        // 发送图片
        event.getChannel().asThreadChannel().sendMessage(imagesInfo).queue();
        // ai报告
        String report = aiSearchFinalEntity.getAiSearchOutputEntity().getReport();
        // 发送ai报告和按钮
        discordWordsLimit.splitParagraphWithActionRow2(report, event, actionRow);
    }

    public void createSearchButtonLoop(ButtonInteractionEvent event, aiSearchFinalEntity aiSearchFinalEntity) {
        List<String> buttonInfo = aiSearchFinalEntity.getAiSearchOutputEntity().getButtonInfo();
        // 创建按钮
        Button option1Button = Button.primary("option1", buttonInfo.get(0));
        Button option2Button = Button.success("option2", buttonInfo.get(1));
        Button option3Button = Button.danger("option3", buttonInfo.get(2));
        Button cancelButton = Button.danger("option4", "cancel");
        ActionRow actionRow = ActionRow.of(option1Button, option2Button, option3Button, cancelButton);
        // 图片
        String imagesInfo = aiSearchFinalEntity.getImagesInfo();
        // 发送图片
        event.getChannel().asThreadChannel().sendMessage(imagesInfo).queue();
        // ai报告
        String report = aiSearchFinalEntity.getAiSearchOutputEntity().getReport();
        // 发送ai报告和按钮
        discordWordsLimit.splitParagraphWithActionRow(report, event, actionRow);
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        // 首先延迟回复，这会让 Discord 知道我们正在处理这个交互
        event.deferReply(false).queue();
        String buttonId = event.getComponentId();
        String id = event.getChannel().asThreadChannel().getParentChannel().getId();
        switch (buttonId) {
            case "option1":
                // 重新搜索
                String label = event.getButton().getLabel();
                // 这个event.getHook()是用来发送消息的，必须在业务逻辑运行前就要调用，不然discord会报错
                event.getHook().sendMessage("搜索option1：" + label).queue();
                AiMessageFormat aiMessageFormat = new AiMessageFormat();
                aiMessageFormat.setMessage(label);
                LangChain4jService langchain4jservice = RegularConfig.getLangchain4jservice();
                aiSearchFinalEntity aisearchResult = langchain4jservice.aisearch(id, aiMessageFormat);
                // 创建带按钮的搜索结果
                createSearchButtonLoop(event, aisearchResult);
                // 使用 hook 发送回复
                break;
            case "option2":
                // 重新搜索
                String label2 = event.getButton().getLabel();
                event.getHook().sendMessage("搜索option2：" + label2).queue();
                AiMessageFormat aiMessageFormat2 = new AiMessageFormat();
                aiMessageFormat2.setMessage(label2);
                LangChain4jService langchain4jservice2 = RegularConfig.getLangchain4jservice();
                aiSearchFinalEntity aisearchResult2 = langchain4jservice2.aisearch(id, aiMessageFormat2);
                // 创建带按钮的搜索结果
                createSearchButtonLoop(event, aisearchResult2);
                break;
            case "option3":
                // 重新搜索
                String label3 = event.getButton().getLabel();
                event.getHook().sendMessage("搜索option3：" + label3).queue();
                AiMessageFormat aiMessageFormat3 = new AiMessageFormat();
                aiMessageFormat3.setMessage(label3);
                LangChain4jService langchain4jservice3 = RegularConfig.getLangchain4jservice();
                aiSearchFinalEntity aisearchResult3 = langchain4jservice3.aisearch(id, aiMessageFormat3);
                // 创建带按钮的搜索结果
                createSearchButtonLoop(event, aisearchResult3);
                break;
            case "option4":
                // 取消搜索
                event.getHook().sendMessage("搜索已取消!").queue();
                // 利用consumer删除最近的两条消息
                event.getChannel().getHistory().retrievePast(2).queue(new Consumer<List<Message>>() {
                    @Override
                    public void accept(List<Message> messages) {
                        for (Message message : messages) {
                            message.delete().queue();
                        }
                    }
                });
                break;
        }
    }

    public String grok(MessageReceivedEvent event) {
        String message = event.getMessage().getContentRaw();

        // 设置 URL 和请求头
        String url = "https://api.x.ai/v1/chat/completions";
        String apiKey = "xai-VLwRbjzhCBejfSBY4SsRrE3nS4j1PtdJKgpgGG9QlPcsDmg9pYbETMPwJfnGzqfPPiq6t7NPZ5iuykxt"; // 替换为实际的API密钥

        // 构建请求体数据
        Map<String, Object> requestData = new HashMap<>();
        requestData.put("model", "grok-beta");
        requestData.put("stream", false);
        requestData.put("temperature", 0);

        // 构建消息数组
        List<Map<String, String>> messages = new ArrayList<>();

        // 系统消息
        Map<String, String> systemMessage = new HashMap<>();
        systemMessage.put("role", "system");
        systemMessage.put("content", "You are Grok, a chatbot inspired by the Hitchhikers Guide to the Galaxy.");
        messages.add(systemMessage);

        // 用户消息
        Map<String, String> userMessage = new HashMap<>();
        userMessage.put("role", "user");
        userMessage.put("content", message);
        messages.add(userMessage);
        requestData.put("messages", messages);
        try {
            // 发送POST请求
            HttpResponse response = HttpRequest.post(url)
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + apiKey)
                    .body(JSONUtil.toJsonStr(requestData))
                    .execute();

            // 解析响应
            String body = response.body();
            JSONObject jsonObject = JSONUtil.parseObj(body);
            String grokResult = jsonObject.getByPath("choices[0].message.content", String.class);
            return grokResult;
        } catch (Exception e) {
            e.printStackTrace();
            return "Sorry, there was an error processing your request: " + e.getMessage();
        }
    }

    private String gemini(MessageReceivedEvent event) {
        String message = event.getMessage().getContentRaw();
        LangChain4jService langchain4jservice = RegularConfig.getLangchain4jservice();
        String id = event.getChannel().asThreadChannel().getParentChannel().getId();
        AiMessageFormat aiMessageFormat = new AiMessageFormat();
        aiMessageFormat.setMessage(message);
        String result = "";
        try {
            result = langchain4jservice.gemini(id , aiMessageFormat);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }





}