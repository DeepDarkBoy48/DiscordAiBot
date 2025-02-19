package robin.discordbot2.tutorial;

import dev.langchain4j.community.model.dashscope.QwenChatModel;
import dev.langchain4j.community.model.dashscope.QwenEmbeddingModel;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.output.Response;
import io.github.cdimascio.dotenv.Dotenv;

public class qwen {
    public static Dotenv dotenv = Dotenv.load();

    public static String getGeminiToken() {
        return dotenv.get("qwen");
    }


    public static void main(String[] args) {
        ChatLanguageModel qwenChatModel = QwenChatModel.builder()
                .apiKey(getGeminiToken())
                .modelName("deepseek-r1")
                .build();


        EmbeddingModel embeddingModel  = QwenEmbeddingModel.builder()
                .apiKey(getGeminiToken())
                .modelName("text-embedding-v3")
                .build();
        Response<Embedding> embed = embeddingModel.embed("你好，你叫什么名字？");
        String string = embed.content().toString();
        System.out.println(string);
        String generate = qwenChatModel.generate("你好，你叫什么名字？");
        System.out.println(generate);
    }
}
