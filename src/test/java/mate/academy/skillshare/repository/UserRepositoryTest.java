package mate.academy.skillshare.repository;

import static mate.academy.skillshare.util.UserTestUtil.createTestUser;
import static org.junit.jupiter.api.Assertions.assertEquals;

import mate.academy.skillshare.config.GlobalSetupExtension;
import mate.academy.skillshare.model.User;
import mate.academy.skillshare.repository.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith(GlobalSetupExtension.class)
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Find user by email")
    @Sql(scripts = {
            "classpath:database/roles/delete-roles.sql",
            "classpath:database/roles/add-roles.sql",
            "classpath:database/users/add-user.sql",
            "classpath:database/users_roles/set-user-role-for-bob.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(scripts = {
            "classpath:database/users_roles/remove-roles-from-users.sql",
            "classpath:database/users/delete-users.sql",
            "classpath:database/roles/delete-roles.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    public void findByEmail_GivenValidEmail_ShouldReturnUser() {
        User expected = createTestUser();
        User actual = userRepository.findByEmail("bob@example.test").get();

        assertEquals(expected.getEmail(), actual.getEmail());
    }
}
