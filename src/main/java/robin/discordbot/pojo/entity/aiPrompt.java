package robin.discordbot.pojo.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class aiPrompt {
    Integer id;
    String prompt;
    String creator;
    LocalDateTime creationTime;
    String modelName;

    public aiPrompt() {
    }

    public aiPrompt(Integer id, String prompt, String creator, LocalDateTime createTime, String modelName) {
        this.id = id;
        this.prompt = prompt;
        this.creator = creator;
        this.creationTime = createTime;
        this.modelName = modelName;
    }

    public aiPrompt(Integer id, String prompt, String creator, LocalDateTime createTime) {
        this.id = id;
        this.prompt = prompt;
        this.creator = creator;
        this.creationTime = createTime;
    }
    public aiPrompt(Integer id, String creator, LocalDateTime createTime, String modelName) {
        this.id = id;
        this.creator = creator;
        this.creationTime = createTime;
        this.modelName = modelName;
    }
}

