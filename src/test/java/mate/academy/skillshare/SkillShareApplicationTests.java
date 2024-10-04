package mate.academy.skillshare;

import mate.academy.skillshare.config.GlobalSetupExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@ExtendWith(GlobalSetupExtension.class)
class SkillShareApplicationTests {
    @Test
    void contextLoads() {
    }
}
