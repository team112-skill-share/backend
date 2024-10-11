package mate.academy.skillshare.repository.course;

import java.util.Optional;
import mate.academy.skillshare.model.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
    @EntityGraph(attributePaths = {"category", "reviews.user.roles"})
    Optional<Course> findById(Long id);

    @EntityGraph(attributePaths = {"category", "reviews.user.roles"})
    Page<Course> findAll(Pageable pageable);
}
