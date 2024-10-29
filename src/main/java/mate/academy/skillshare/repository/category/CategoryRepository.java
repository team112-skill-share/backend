package mate.academy.skillshare.repository.category;

import java.util.Optional;
import mate.academy.skillshare.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String name);
}
