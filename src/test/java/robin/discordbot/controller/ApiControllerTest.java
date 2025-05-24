package robin.discordbot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import robin.discordbot.service.LangChain4jService;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ApiController.class)
public class ApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LangChain4jService langChain4jService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testChatWithBot() throws Exception {
        String userMessage = "Hello, AI!";
        String aiResponse = "Hello, User!";

        when(langChain4jService.chat(userMessage)).thenReturn(aiResponse);

        mockMvc.perform(post("/api/chat")
                        .contentType(MediaType.APPLICATION_JSON) // Assuming the controller expects JSON
                        .content(userMessage)) // Sending raw string as content
                .andExpect(status().isOk())
                .andExpect(content().string(aiResponse));

        verify(langChain4jService).chat(userMessage);
    }
}
