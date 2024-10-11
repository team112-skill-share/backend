package mate.academy.skillshare.repository.comment;

import java.util.Optional;
import mate.academy.skillshare.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @EntityGraph(attributePaths = {"article", "user.roles"})
    Optional<Comment> findById(Long id);

    @EntityGraph(attributePaths = {"article", "user.roles"})
    Page<Comment> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"article", "user.roles"})
    Page<Comment> findAllByArticleId(Long articleId, Pageable pageable);
}
