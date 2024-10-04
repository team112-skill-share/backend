package mate.academy.skillshare;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SkillShareApplication {
    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.configure().load();
        System.setProperty("MYSQL_ROOT_PASSWORD", dotenv.get("MYSQL_ROOT_PASSWORD"));
        System.setProperty("JWT_SECRET", dotenv.get("JWT_SECRET"));
        System.setProperty("JWT_EXPIRATION", dotenv.get("JWT_EXPIRATION"));
        SpringApplication.run(SkillShareApplication.class, args);
    }
}
