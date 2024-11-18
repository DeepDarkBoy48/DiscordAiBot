//package robin.discordbot2.listener;
//
//import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
//import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
//import net.dv8tion.jda.api.hooks.ListenerAdapter;
//import net.dv8tion.jda.api.interactions.components.buttons.Button;
//import net.dv8tion.jda.api.interactions.components.ActionRow;
//import org.springframework.stereotype.Service;
//import robin.discordbot2.config.RegularConfig;
//import robin.discordbot2.pojo.entity.aiEntity.AiMessageFormat;
//import robin.discordbot2.service.LangChain4jService;
//
//@Service
//public class NSFWListener extends ListenerAdapter {
//
//    @Override
//    public void onMessageReceived(MessageReceivedEvent event) {
//        // 检查是否是目标频道
//        if (event.isFromThread() &&
//                event.getChannel().asThreadChannel().getParentChannel().getName().equals("还要做多久（劲爆版🔞）")) {
//
//            // 忽略机器人消息
//            if (event.getMessage().getAuthor().isBot()) {
//                return;
//            }
//
//            // 获取消息内容
//            String message = event.getMessage().getContentRaw();
//
//
//            handleNSFWSearch(event, message);
//
//
//        }
//    }
//
//
//    private void handleNSFWSearch(MessageReceivedEvent event, String message) {
//        // 创建消息格式
//        AiMessageFormat aiMessageFormat = new AiMessageFormat();
//        aiMessageFormat.setMessage(message);
//
//        // 获取服务并执行搜索
//        LangChain4jService langchain4jservice = RegularConfig.getLangchain4jservice();
//        String searchResult = langchain4jservice.aisearch(
//                event.getChannel().getId(),
//                aiMessageFormat
//        );
//
//        // 创建按钮
//        createSearchButtons(event, searchResult);
//    }
//
//    private void createSearchButtons(MessageReceivedEvent event, String searchResult) {
//        // 创建按钮
//        Button searchAgainButton = Button.primary("nsfw_search_again", "重新搜索");
//        Button saveButton = Button.success("nsfw_save_result", "保存结果");
//        Button cancelButton = Button.danger("nsfw_cancel_search", "取消");
//
//        // 创建按钮行
//        ActionRow actionRow = ActionRow.of(searchAgainButton, saveButton, cancelButton);
//
//        // 发送结果和按钮
//        event.getChannel().asThreadChannel()
//                .sendMessage(searchResult)
//                .setComponents(actionRow)
//                .queue();
//    }
//
//    @Override
//    public void onButtonInteraction(ButtonInteractionEvent event) {
//        String buttonId = event.getComponentId();
//
//        switch (buttonId) {
//            case "search_again":
//                // 重新搜索
//                event.reply("请输入新的搜索关键词:").queue();
//                break;
//            case "save_result":
//                // 保存结果
//                event.reply("搜索结果已保存!").queue();
//                // 这里可以添加保存到数据库的逻辑
//                break;
//            case "cancel_search":
//                // 取消搜索
//                event.reply("搜索已取消!").queue();
//                event.getMessage().delete().queue(); // 删除原消息
//                break;
//        }
//    }
//}