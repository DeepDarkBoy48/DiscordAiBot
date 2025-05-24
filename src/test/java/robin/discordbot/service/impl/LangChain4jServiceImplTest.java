package robin.discordbot.service.impl;

import dev.langchain4j.model.chat.ChatLanguageModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import robin.discordbot.service.LangChain4jService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LangChain4jServiceImplTest {

    @Mock
    private ChatLanguageModel chatLanguageModel;

    @InjectMocks
    private LangChain4jServiceImpl langChain4jService;

    @BeforeEach
    public void setUp() {
        // Initialize mocks created with @Mock and inject them into @InjectMocks fields
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testChat() {
        String userMessage = "Hello, LangChain4j!";
        String expectedResponse = "Hello, User!";

        // Mock the behavior of chatLanguageModel.generate()
        when(chatLanguageModel.generate(userMessage)).thenReturn(expectedResponse);

        // Call the method to be tested
        String actualResponse = langChain4jService.chat(userMessage);

        // Verify that chatLanguageModel.generate() was called with the correct message
        verify(chatLanguageModel).generate(userMessage);

        // Verify that the method returned the expected response
        assertEquals(expectedResponse, actualResponse);
    }
}
