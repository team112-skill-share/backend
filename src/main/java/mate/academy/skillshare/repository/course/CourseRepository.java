package mate.academy.skillshare.repository.course;

import java.util.Optional;
import mate.academy.skillshare.model.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CourseRepository extends JpaRepository<Course, Long>,
        JpaSpecificationExecutor<Course> {
    @EntityGraph(attributePaths = {"category", "reviews.user.roles", "contents", "images"})
    Optional<Course> findById(Long id);

    @EntityGraph(attributePaths = {"category", "reviews.user.roles", "contents", "images"})
    Page<Course> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"category", "reviews.user.roles", "contents", "images"})
    Page<Course> findAll(Specification<Course> courseSpecification, Pageable pageable);
}
