import com.zeroone.star.gateway.GatewayApplication;
import com.zeroone.star.project.constant.RedisConstant;
import org.apache.http.auth.AUTH;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;


@SpringBootTest(classes = GatewayApplication.class)
public class TestClass {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    
    // TODO: 添加测试方法
    // 示例测试方法结构：
     @Test
     public void testSomething() {
         String token = "1";
//         redisTemplate.opsForValue().set(RedisConstant.LOGOUT_TOKEN_PREFIX + token, RedisConstant.TOKEN_STATUS_ACTIVE);
//         redisTemplate.opsForValue().set(
//                 RedisConstant.LOGOUT_TOKEN_PREFIX + token,
//                 RedisConstant.TOKEN_STATUS_ACTIVE,
//                 6000,
//                 TimeUnit.SECONDS
//         );
         String s = (String)redisTemplate.opsForValue().get(
                 token);
         System.out.println(s);

     }
    
}
