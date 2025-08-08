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
import robin.discordbot.config.WebSocketConfig;
import robin.discordbot.pojo.entity.User;
import robin.discordbot.service.MainChannelAIService;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint("/chat/{sid}")
public class WebSocketServer {

    private static Map<String, Session> sessionMap = new ConcurrentHashMap<>();
    private User user;

    /**
     * English: Method called when a connection is established
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("sid") String sid) {
        System.out.println("client:" + sid + " connected");
        user = RegularConfig.getUserService().getUserById(Integer.parseInt(sid));
        String nickname = user.getNickname();
        sessionMap.put(sid, session);
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.set("from", "server");
        jsonResponse.set("message", "<strong>AI:</strong> " + "user <strong>" + nickname + "</strong> connected<br>" +
                "<span>tutorial:</span><br>" +
                "<div>Use the following command to talk to ai</div>" +
                "<span><strong>@keyword</strong> [enter keyword to search relevant feedbacks from the excel file]</span><br>" +
                "<span><strong>@sentiment</strong> [enter a sentence to analyze sentiment]</span><br>" +
                "<span><strong>@summary</strong> [enter single or multiple course module to get ai summary]</span>");
        sendToClient(sid, jsonResponse.toString());
    }

    @OnMessage
    public void onMessage(String message, @PathParam("sid") String sid) {

        System.out.println("receive message from client: " + sid + user.getNickname() + message);
        // Create a JSON object to store the response message
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.set("from", "server");
        // Use WebSocketServerConfig to get MyAiService instance

        //Call AI service
        // 这是:
        MainChannelAIService AiService = RegularConfig.getMainWebChannelAIServiceImplAGENT();
        String s = AiService.aiWebAGENT(user, message);
        if (s != null) {
            jsonResponse.set("message", "<strong>AI:</strong> " + s);
        } else {
            jsonResponse.set("message", "<strong>AI:</strong> " + "AI service is not available");
        }

        //Convert the JSON object to a string and send it to the client
        sendToClient(sid, jsonResponse.toString());
    }

    public void sendToClient(String sid, String message) {
        Session session = sessionMap.get(sid);
        if (session != null) {
            try {
                session.getBasicRemote().sendText(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("user " + sid + " not connected");
        }
    }

    /**
     * Method called when a connection is closed
     */
    @OnClose
    public void onClose(@PathParam("sid") String sid) {
        System.out.println("Connection close:" + sid);
        sessionMap.remove(sid);
    }


    public void sendToServer(String message) {
        for (Session session : sessionMap.values()) {
            try {
                session.getBasicRemote().sendText(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}