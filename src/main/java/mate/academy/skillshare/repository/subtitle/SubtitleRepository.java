package mate.academy.skillshare.repository.subtitle;

import java.util.Optional;
import mate.academy.skillshare.model.Subtitle;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubtitleRepository extends JpaRepository<Subtitle, Long> {
    @EntityGraph(attributePaths = {"article.contents", "article.images"})
    Optional<Subtitle> findById(Long id);
}
