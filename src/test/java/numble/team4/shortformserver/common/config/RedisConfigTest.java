package numble.team4.shortformserver.common.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("local")
@SpringBootTest(classes = {RedisConfig.class})
class RedisConfigTest {

    @Autowired
    RedisTemplate redisTemplate;

    @Test
    @DisplayName("Redis key-value 저장 테스트")
    void saveRedisTest() {
        //given
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();

        //when
        valueOperations.set("test", "value");

        //then
        assertThat(valueOperations.get("test")).isEqualTo("value");
        redisTemplate.delete("test");
    }

}