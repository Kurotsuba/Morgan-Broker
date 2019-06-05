package group.eis.morganborker;

import group.eis.morganborker.utils.RedisUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class RedisUtilsTest {

    @Autowired
    RedisUtil redisUtil;

    @Test
    public void testListPushPop(){
        redisUtil.listPush("test1", 0);
        redisUtil.listPush("test1", 1);
        redisUtil.listPush("test1", 2);
        Integer value = (Integer) redisUtil.listPop("test");
        System.out.println(value);
        value = (Integer) redisUtil.listPop("test");
        System.out.println(value);
    }
}
