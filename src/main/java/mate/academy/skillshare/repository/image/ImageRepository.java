package mate.academy.skillshare.repository.image;

import java.util.Optional;
import mate.academy.skillshare.model.Image;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
    @EntityGraph(attributePaths = {
            "course.category",
            "course.contents",
            "article.subtitles",
            "article.contents"
    })
    Optional<Image> findById(Long id);
}
