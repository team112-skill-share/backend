package mate.academy.skillshare.repository.article;

import java.util.Optional;
import mate.academy.skillshare.model.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    @EntityGraph(attributePaths = "comments.user.roles")
    Optional<Article> findById(Long id);

    @EntityGraph(attributePaths = "comments.user.roles")
    Page<Article> findAll(Pageable pageable);
}
