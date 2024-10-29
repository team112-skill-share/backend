package mate.academy.skillshare.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import mate.academy.skillshare.config.GlobalSetupExtension;
import mate.academy.skillshare.model.Review;
import mate.academy.skillshare.repository.review.ReviewRepository;
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
public class ReviewRepositoryTest {
    @Autowired
    private ReviewRepository reviewRepository;

    @Test
    @DisplayName("Find reviews by course id")
    @Sql(scripts = {
            "classpath:database/users/add-user.sql",
            "classpath:database/categories/add-category.sql",
            "classpath:database/courses/add-course.sql",
            "classpath:database/reviews/add-two-reviews.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(scripts = {
            "classpath:database/reviews/delete-reviews.sql",
            "classpath:database/courses/delete-courses.sql",
            "classpath:database/categories/delete-categories.sql",
            "classpath:database/users/delete-users.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    public void findAllByCourseId_GivenCourseUserId_ShouldReturnCourse() {
        Pageable pageable = PageRequest.of(0, 20);
        Page<Review> page = reviewRepository.findAllByCourseId(1L, pageable);
        List<Review> actual = page.getContent();

        assertEquals(2, actual.size());
    }
}
