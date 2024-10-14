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
        System.setProperty("GOOGLE_CLIENT_ID", dotenv.get("GOOGLE_CLIENT_ID"));
        System.setProperty("GOOGLE_CLIENT_SECRET", dotenv.get("GOOGLE_CLIENT_SECRET"));
        SpringApplication.run(SkillShareApplication.class, args);
    }
}
