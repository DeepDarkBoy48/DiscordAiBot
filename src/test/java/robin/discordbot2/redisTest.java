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
    


    @Test
    public void testRedisson() {
        // 使用 Redisson 客户端获取一个 RMap 实例
        RMap<String, String> map = redissonClient.getMap("tutorial-map");

        // 在 Redis 中设置一个键值对
        map.put("tutorial-name", "Redisson 教程");

        // 获取与键相关的值
        String value = map.get("tutorial-name");
        System.out.println("在 Redis 中存储的字符串: " + value);
    }
}
