package mate.academy.skillshare.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import mate.academy.skillshare.config.GlobalSetupExtension;
import mate.academy.skillshare.model.Comment;
import mate.academy.skillshare.repository.comment.CommentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith(GlobalSetupExtension.class)
public class CommentRepositoryTest {
    @Autowired
    private CommentRepository commentRepository;

    @Test
    @DisplayName("Find comments by article id")
    @Sql(scripts = {
            "classpath:database/articles/delete-articles.sql",
            "classpath:database/users/add-user.sql",
            "classpath:database/articles/add-article.sql",
            "classpath:database/comments/add-two-comments.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(scripts = {
            "classpath:database/comments/delete-comments.sql",
            "classpath:database/articles/delete-articles.sql",
            "classpath:database/users/delete-users.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    public void findAllByArticleId_GivenCourseUserId_ShouldReturnCourse() {
        Pageable pageable = PageRequest.of(0, 20);
        Page<Comment> page = commentRepository.findAllByArticleId(1L, pageable);
        List<Comment> actual = page.getContent();

        assertEquals(2, actual.size());
    }
}
