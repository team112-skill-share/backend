package mate.academy.skillshare.repository.category;

import mate.academy.skillshare.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
