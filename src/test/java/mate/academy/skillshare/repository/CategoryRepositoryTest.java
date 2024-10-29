package mate.academy.skillshare.repository;

import static mate.academy.skillshare.util.CategoryTestUtil.createTestCategory;
import static org.junit.jupiter.api.Assertions.assertEquals;

import mate.academy.skillshare.config.GlobalSetupExtension;
import mate.academy.skillshare.model.Category;
import mate.academy.skillshare.repository.category.CategoryRepository;
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
public class CategoryRepositoryTest {
    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("Find category by name")
    @Sql(scripts = {
            "classpath:database/categories/delete-categories.sql",
            "classpath:database/categories/add-category.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(scripts = {
            "classpath:database/categories/delete-categories.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    public void findByName_GivenValidName_ShouldReturnCategory() {
        Category expected = createTestCategory();
        Category actual = categoryRepository.findByName(expected.getName()).get();

        assertEquals(expected.getName(), actual.getName());
    }
}
