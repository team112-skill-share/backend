package mate.academy.skillshare.repository.content;

import java.util.Optional;
import mate.academy.skillshare.model.Content;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContentRepository extends JpaRepository<Content, Long> {
    @EntityGraph(attributePaths = {
            "course.category",
            "course.images",
            "article.subtitles",
            "article.images"
    })
    Optional<Content> findById(Long id);
}
