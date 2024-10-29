package mate.academy.skillshare.repository;

import static mate.academy.skillshare.util.UserTestUtil.createUserRole;
import static org.junit.jupiter.api.Assertions.assertEquals;

import mate.academy.skillshare.config.GlobalSetupExtension;
import mate.academy.skillshare.model.Role;
import mate.academy.skillshare.repository.role.RoleRepository;
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
public class RoleRepositoryTest {
    @Autowired
    private RoleRepository roleRepository;

    @Test
    @DisplayName("Find role by roleName")
    @Sql(scripts = {
            "classpath:database/roles/delete-roles.sql",
            "classpath:database/roles/add-roles.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(scripts = {
            "classpath:database/roles/delete-roles.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    public void findByRoleName_GivenValidRoleName_ShouldReturnRole() {
        Role expected = createUserRole();
        Role actual = roleRepository.findByRoleName(Role.RoleName.ROLE_USER);

        assertEquals(expected.getRoleName(), actual.getRoleName());
    }
}
