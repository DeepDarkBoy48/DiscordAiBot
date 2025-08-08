package robin.discordbot.websocket;

import cn.hutool.json.JSONObject;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.stereotype.Component;
import robin.discordbot.config.RegularConfig;
import robin.discordbot.pojo.entity.ChatMessage;
import robin.discordbot.pojo.entity.User;
import robin.discordbot.service.ChatMessageService;
import robin.discordbot.service.MainChannelAIService;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint("/chat/{sid}")
public class WebSocketServer {

    private static Map<String, Session> sessionMap = new ConcurrentHashMap<>();
    private User user;

    @OnOpen
    public void onOpen(Session session, @PathParam("sid") String sid) {
        System.out.println("client:" + sid + " connected");
        this.user = RegularConfig.getUserService().getUserById(Integer.parseInt(sid));
        sessionMap.put(sid, session);

        // Load and send recent messages
        ChatMessageService chatMessageService = RegularConfig.getChatMessageService();
        List<ChatMessage> history = chatMessageService.getRecentMessages("global", 50);
        for (int i = history.size() - 1; i >= 0; i--) {
            ChatMessage msg = history.get(i);
            JSONObject historyMsg = new JSONObject();
            historyMsg.set("from", msg.getSenderNickname());
            historyMsg.set("message", msg.getMessageContent());
            sendToClient(session, historyMsg.toString());
        }

        // Broadcast connection message
        JSONObject connectionMessage = new JSONObject();
        connectionMessage.set("from", "server");
        connectionMessage.set("message", "<strong>" + user.getNickname() + "</strong> has joined the chat.<br>");
        broadcast(connectionMessage.toString());

    }

    @OnMessage
    public void onMessage(String message, @PathParam("sid") String sid) {
        System.out.println("receive message from client: " + sid + " (" + this.user.getNickname() + "): " + message);

        // Broadcast user message
        JSONObject userMessage = new JSONObject();
        userMessage.set("from", this.user.getNickname());
        userMessage.set("message", message);
        broadcast(userMessage.toString());

        // Save user message
        ChatMessageService chatMessageService = RegularConfig.getChatMessageService();
        chatMessageService.saveMessage(new ChatMessage(this.user.getId().longValue(), this.user.getNickname(), message, "global"));

        // Get AI response
        MainChannelAIService AiService = RegularConfig.getMainWebChannelAIServiceImplAGENT();
        String aiResponseText = AiService.aiWebAGENT(this.user, message);

        // Broadcast AI response
        JSONObject aiResponseMessage = new JSONObject();
        aiResponseMessage.set("from", "AI");
        if (aiResponseText != null) {
            aiResponseMessage.set("message", aiResponseText);
        } else {
            aiResponseMessage.set("message", "AI service is not available");
        }
        broadcast(aiResponseMessage.toString());

        // Save AI message
        chatMessageService.saveMessage(new ChatMessage(-1L, "AI", aiResponseText, "global"));
    }

    @OnClose
    public void onClose(@PathParam("sid") String sid) {
        System.out.println("Connection close:" + sid);
        sessionMap.remove(sid);
        if (this.user != null) {
            JSONObject disconnectMessage = new JSONObject();
            disconnectMessage.set("from", "server");
            disconnectMessage.set("message", "<strong>" + this.user.getNickname() + "</strong> has left the chat.<br>");
            broadcast(disconnectMessage.toString());
        }
    }

    private synchronized void sendToClient(Session session, String message) {
        try {
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            System.err.println("Error sending message to client: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private synchronized void broadcast(String message) {
        for (Session session : sessionMap.values()) {
            if (session.isOpen()) {
                sendToClient(session, message);
            }
        }
    }
}
