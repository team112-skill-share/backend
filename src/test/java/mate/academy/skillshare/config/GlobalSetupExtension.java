package mate.academy.skillshare.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class GlobalSetupExtension implements BeforeAllCallback {
    @Override
    public void beforeAll(ExtensionContext extensionContext) throws Exception {
        if (System.getenv("CI") == null) {
            Dotenv dotenv = Dotenv.configure().load();
            System.setProperty("SPRING_MAIL_USERNAME", dotenv.get("SPRING_MAIL_USERNAME"));
            System.setProperty("SPRING_MAIL_PASSWORD", dotenv.get("SPRING_MAIL_PASSWORD"));
            System.setProperty("JWT_SECRET", dotenv.get("JWT_SECRET"));
            System.setProperty("JWT_EXPIRATION", dotenv.get("JWT_EXPIRATION"));
            System.setProperty("JWT_PASSWORD_RESET_EXPIRATION",
                    dotenv.get("JWT_PASSWORD_RESET_EXPIRATION"));
            System.setProperty("GOOGLE_CLIENT_ID", dotenv.get("GOOGLE_CLIENT_ID"));
            System.setProperty("GOOGLE_CLIENT_SECRET", dotenv.get("GOOGLE_CLIENT_SECRET"));
            System.setProperty("COURSE_EMAIL", dotenv.get("COURSE_EMAIL"));
        } else {
            System.setProperty("SPRING_MAIL_USERNAME", System.getenv("SPRING_MAIL_USERNAME"));
            System.setProperty("SPRING_MAIL_PASSWORD", System.getenv("SPRING_MAIL_PASSWORD"));
            System.setProperty("JWT_SECRET", System.getenv("JWT_SECRET"));
            System.setProperty("JWT_EXPIRATION", System.getenv("JWT_EXPIRATION"));
            System.setProperty("JWT_PASSWORD_RESET_EXPIRATION",
                    System.getenv("JWT_PASSWORD_RESET_EXPIRATION"));
            System.setProperty("GOOGLE_CLIENT_ID", System.getenv("GOOGLE_CLIENT_ID"));
            System.setProperty("GOOGLE_CLIENT_SECRET", System.getenv("GOOGLE_CLIENT_SECRET"));
            System.setProperty("COURSE_EMAIL", System.getenv("COURSE_EMAIL"));
        }
    }
}
