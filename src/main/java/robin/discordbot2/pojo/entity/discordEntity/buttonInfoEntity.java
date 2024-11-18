package robin.discordbot2.pojo.entity.discordEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class buttonInfoEntity {
    private List<String> buttonContent;
}
