package group.eis.morganborker.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public final class RedisUtil {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public boolean setExpire(String key, long time) {
        try{
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }

    public long getExpire(String key){
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    public boolean hasKey(String key){
        return redisTemplate.hasKey(key);
    }

    @SuppressWarnings("unchecked")
    public void delete(String... key){
        if(key != null && key.length > 0){
            if(key.length == 1){
                redisTemplate.delete(key[0]);
            }else{
                redisTemplate.delete(CollectionUtils.arrayToList(key));
            }
        }
    }

    public Object get(String key){
        if(hasKey(key)){
            return redisTemplate.opsForValue().get(key);
        }else{
            return null;
        }
    }

    public boolean set(String key, Object value){
        try{
            redisTemplate.opsForValue().set(key, value);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean set(String key, Object value, long time){
        try{
            if (time > 0){
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            }else{
                set(key, value);
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public long increment(String key, long delta){
        if(delta >= 0){
            return redisTemplate.opsForValue().increment(key, delta);
        }
        throw new RuntimeException("Increment delta should larger than 0");
    }

    public long decrement(String key, long delta){
        if(delta >= 0){
            return redisTemplate.opsForValue().decrement(key, delta);
        }
        throw new RuntimeException("Decrement delta should larger than 0");
    }

    public Object hashGet(String key, String item){
        if(hasKey(key)){
            return redisTemplate.opsForHash().get(key, item);
        }
        return null;
    }

    public Map<Object, Object> hashMapGet(String key){
        if(hasKey(key)){
            return redisTemplate.opsForHash().entries(key);
        }
        return null;
    }

    public boolean hashMapSet(String key, Map<String, Object> map){
        try{
            redisTemplate.opsForHash().putAll(key, map);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean hashMapSet(String key, Map<String, Object> map, long time){
        try{
            redisTemplate.opsForHash().putAll(key, map);
            if(time > 0){
                setExpire(key, time);
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean hashSet(String key, String item, Object value){
        try{
            redisTemplate.opsForHash().put(key, item, value);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public void hashDel(String key, Object... item){
        redisTemplate.opsForHash().delete(key, item);
    }

    public boolean hashHasKeyItem(String key, String item){
        return redisTemplate.opsForHash().hasKey(key, item);
    }

    public long hashIncrement(String key, String item, long delta){
        if(hashHasKeyItem(key, item)){
            return redisTemplate.opsForHash().increment(key, item, delta);
        }
        return 0l;
    }
}
