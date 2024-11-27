package robin.discordbot2;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class redisTest {
    @Resource
    private RedissonClient redissonClient;
    @Test
    public void test(){
        RMap<Object, Object> test1 = redissonClient.getMap("test1");
        test1.put("name", "robin");
        System.out.println(test1.get("name"));
    }
}

