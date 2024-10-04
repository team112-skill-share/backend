package mate.academy.skillshare.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DotenvConfig {
    @Bean
    public Dotenv dotenv() {
        try {
            return Dotenv.load();
        } catch (Exception e) {
            System.out.println("Loading from environment variables instead of .env file.");
            return Dotenv.configure().ignoreIfMissing().load();
        }
    }
}
