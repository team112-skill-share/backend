package mate.academy.skillshare.repository.review;

import java.util.Optional;
import mate.academy.skillshare.model.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    @EntityGraph(attributePaths = {"course.category", "user.roles"})
    Optional<Review> findById(Long id);

    @EntityGraph(attributePaths = {"course.category", "user.roles"})
    Page<Review> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"course.category", "user.roles"})
    Page<Review> findAllByCourseId(Long courseId, Pageable pageable);
}
