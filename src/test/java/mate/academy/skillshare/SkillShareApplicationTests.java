package mate.academy.skillshare;

import io.github.cdimascio.dotenv.Dotenv;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SkillShareApplicationTests {
    @BeforeAll
    static void setUp() {
        if (System.getenv("CI") == null) {
            // Not in CI, load from .env file
            Dotenv dotenv = Dotenv.configure().load();
            System.setProperty("JWT_SECRET", dotenv.get("JWT_SECRET"));
            System.setProperty("JWT_EXPIRATION", dotenv.get("JWT_EXPIRATION"));
        } else {
            // In CI, load from system environment variables
            System.setProperty("JWT_SECRET", System.getenv("JWT_SECRET"));
            System.setProperty("JWT_EXPIRATION", System.getenv("JWT_EXPIRATION"));
        }
    }

    @Test
    void contextLoads() {
    }
}
