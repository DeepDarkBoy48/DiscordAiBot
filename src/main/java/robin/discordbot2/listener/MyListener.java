package robin.discordbot2.listener;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSON;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Service
public class MyListener extends ListenerAdapter {

    private String globalName = "";

    // 添加反应的事件
    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event) {
        String emoji = event.getReaction().getEmoji().getAsReactionCode();
        String userTag = event.getUser().getGlobalName();
        String jumpLink = event.getJumpUrl();
        String channelMention = event.getChannel().getAsMention();
        // 当前频道
        MessageChannelUnion channel = event.getChannel();
        // 找到常规频道
        // TextChannel textChannel = event.getGuild().getTextChannelsByName("常规",
        // true).stream().findFirst().orElse(null);
        // 检查频道是否存在并发送消息
        if (channel != null) {
            channel.sendMessage(
                    "用户 " + userTag + " 反应了 " + emoji + " in " + channelMention + ".\nJump to message: " + jumpLink)
                    .queue();
        } else {
            System.out.println("Channel '常规' not found!");
        }
    }

    // 发消息的事件

    /**
     * Event fires when a message is sent in discord.
     * Warning: Will require "Guild Messages" gateway intent after August 2022!
     */
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        // After August 2022 will require gateway intent
        String message = event.getMessage().getContentRaw();
        // FileUpload fileUpload = FileUpload.fromData(new File("static/招募.png"),
        // "招募.png");
        if (message.contains("ping")) {
            event.getChannel().sendMessage(
                    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTZ8WKYDQJA5ZLhUEL4r-hJqWSV0UjZtPiHOJQaTM5ssaHXjVr5dLTpnWTFZa8hB53StEc&usqp=CAU")
                    .queue();
            event.getChannel().sendMessage("我是DeepDarkBot，你的地牢管家").queue();
        } else if (event.isFromThread()
                && event.getChannel().asThreadChannel().getParentChannel().getName().equals("ai聊天")) {
            // ai thread chat
            // 用户id1121767044715651122
            // 如果是bot的消息1263657178540019763，就不回复
            if (event.getMessage().getAuthor().getId().equals("1263657178540019763")) {
                // System.out.println(event.getMessage().getAuthor().getId());
                return;
            }
            // 获取帖子,获取tag
            ThreadChannel post = event.getChannel().asThreadChannel();
            String tag = "";
            List<ForumTag> appliedTags = post.getAppliedTags();
            // if (appliedTags.contains("awd"))
            for (ForumTag forumTag : appliedTags) {
                if (forumTag.getName().equals("chatgpt-4o-mini")) {
                    tag = "1";
                }
                if (forumTag.getName().equals("perplexity")) {
                    tag = "2";
                }
                if (forumTag.getName().equals("embed")) {
                    tag = "3";
                }
                if (forumTag.getName().equals("aisearch")) {
                    tag = "4";
                }
            }
            listener1(tag, event);
            // 频道id
            String id = event.getChannel().asThreadChannel().getParentChannel().getId();
            switch (tag) {
                case "1":
                    // 帖子id1288696073057079328
                    String postId = post.getId();
                    AiMessageFormat aiMessageFormat = new AiMessageFormat();
                    aiMessageFormat.setMessage(message);
                    LangChain4jService langchain4jservice = RegularConfig.getLangchain4jservice();
                    String deepDarkResult = langchain4jservice.deepDarkTreadAiChat(postId, aiMessageFormat);
                    event.getChannel().asThreadChannel().sendMessage(deepDarkResult).queue();
                    break;
                case "2":

                    // 设置 URL
                    String url = "https://api.perplexity.ai/chat/completions";
                    // 构建请求体数据
                    Map<String, Object> requestData = new HashMap<>();
                    requestData.put("model", "llama-3.1-sonar-large-128k-online");

                    // 消息内容
                    Map<String, String> systemMessage = new HashMap<>();
                    systemMessage.put("role", "system");
                    systemMessage.put("content", "详细");

                    Map<String, String> userMessage = new HashMap<>();
                    userMessage.put("role", "user");
                    userMessage.put("content", message + ",最好提供网址链接");

                    // 将消息添加到请求体
                    requestData.put("messages", new Map[] { systemMessage, userMessage });
                    requestData.put("max_tokens", 500);
                    requestData.put("temperature", 1);
                    requestData.put("top_p", 0.9);
                    requestData.put("return_citations", true);
                    requestData.put("search_domain_filter", new String[] { "perplexity.ai" });
                    requestData.put("return_images", true);
                    requestData.put("return_related_questions", true);
                    requestData.put("search_recency_filter", "month");
                    requestData.put("top_k", 0);
                    requestData.put("stream", false);
                    requestData.put("presence_penalty", 0);
                    requestData.put("frequency_penalty", 1);

                    String jsonString = JSON.toJSONString(requestData);

                    HttpResponse deepDarkResult2 = HttpRequest.post(url)
                            .header("Authorization", "Bearer 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") // 替换
                                                                                                                     // <token>
                                                                                                                     // 为您的实际
                                                                                                                     // API
                                                                                                                     // 令牌
                            .header("Content-Type", "application/json")
                            .body(jsonString) // 设置请求体
                            .execute(); // 执行请求

                    String body = deepDarkResult2.body();

                    // 使用 Hutool 解析 JSON 自动完成了 Unicode 转义字符的解码
                    JSONObject jsonObject = JSONUtil.parseObj(body);
                    String content = jsonObject.getByPath("choices[0].message.content", String.class);

                    // AiMessageFormat aiMessageFormat1 = new AiMessageFormat();
                    // aiMessageFormat1.setMessage(content);
                    // LangChain4jService langchain4jservice1 =
                    // RegularConfig.getLangchain4jservice();
                    // String deepedDarkTreadAiChat =
                    // langchain4jservice1.deepDarkTreadAiChat(id,aiMessageFormat1);
                    // 输出 content 字段
                    event.getChannel().asThreadChannel().sendMessage(content).queue();
                    break;
            }

        }
    }

    // 成员加入事件

    /**
     * Event fires when a new member joins a guild
     * Warning: Will not work without "Guild Members" gateway intent!
     */
    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        // WILL NOT WORK WITHOUT GATEWAY INTENT!
        String avatar = event.getUser().getEffectiveAvatarUrl();
        System.out.println(avatar);
    }

    /**
     * Event fires when a user updates their online status
     * Requires "Guild Presences" gateway intent AND cache enabled!
     */
    @Override
    public void onUserUpdateOnlineStatus(@NotNull UserUpdateOnlineStatusEvent event) {
        int onlineMembers = 0;
        for (Member member : event.getGuild().getMembers()) {
            if (member.getOnlineStatus() == OnlineStatus.ONLINE) {
                onlineMembers++;
            }
        }
        // 使用cache
        User user = event.getJDA().getUserById("1121767044715651122");
        Member member = event.getGuild().getMemberById("1121767044715651122");

        // 不需要cache，使用rest action
        event.getJDA().retrieveUserById("1121767044715651122").queue(new Consumer<User>() {
            @Override
            public void accept(User user) {
                globalName = user.getGlobalName();
            }
        });

        String message = event.getUser().getAsTag() + "updated their online status! There are " + onlineMembers
                + " members online now!";
        // TextChannel textChannel = event.getGuild().getTextChannelsByName("常规",
        // true).stream().findFirst().orElse(null);
        event.getGuild().getTextChannelsByName("常规", true).stream().findFirst().orElse(null).sendMessage(message)
                .queue();
    }

    public String listener1(String tag, MessageReceivedEvent event) {
        String result = "";
        switch (tag) {
            case "1":
                return null;
            case "2":
                return null;
            case "3":
                result = embed(event);
                break;
            case "4":
                result = aisearch(event);
                return null;
        }
        event.getChannel().asThreadChannel().sendMessage(result).queue();
        return result;
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
        //aichat
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

        ActionRow actionRow = ActionRow.of(option1Button, option2Button,option3Button, cancelButton);
        //ai报告
        String report = aiSearchFinalEntity.getAiSearchOutputEntity().getReport();
        //图片
        String imagesInfo = aiSearchFinalEntity.getImagesInfo();
        //发送图片
        event.getChannel().asThreadChannel().sendMessage(imagesInfo).queue();
        // 发送ai报告和按钮
        event.getChannel().asThreadChannel().sendMessage(report).setComponents(actionRow).queue();
    }

    public void createSearchButtonLoop( ButtonInteractionEvent event,aiSearchFinalEntity aiSearchFinalEntity) {
        List<String> buttonInfo = aiSearchFinalEntity.getAiSearchOutputEntity().getButtonInfo();

        // 创建按钮
        Button option1Button = Button.primary("option1", buttonInfo.get(0));
        Button option2Button = Button.success("option2", buttonInfo.get(1));
        Button option3Button = Button.danger("option3", buttonInfo.get(2));
        Button cancelButton = Button.danger("option4", "cancel");
        ActionRow actionRow = ActionRow.of(option1Button, option2Button, option3Button,cancelButton);
        //ai报告
        String report = aiSearchFinalEntity.getAiSearchOutputEntity().getReport();
        //图片
        String imagesInfo = aiSearchFinalEntity.getImagesInfo();
        //发送图片
        event.getChannel().asThreadChannel().sendMessage(imagesInfo).queue();
        // 发送ai报告和按钮
        event.getChannel().asThreadChannel().sendMessage(report).setComponents(actionRow).queue();

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
                //利用consumer删除最近的两条消息
                event.getChannel().getHistory().retrievePast(2).queue(new Consumer<List<Message>>() {
                    @Override
                    public void accept(List<Message> messages) {
                        for (Message message : messages){
                            message.delete().queue();
                        }
                    }
                });
                break;
        }
    }

}