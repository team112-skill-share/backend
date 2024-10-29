package mate.academy.skillshare.util;

import static mate.academy.skillshare.util.CourseTestUtil.createTestCourse;
import static mate.academy.skillshare.util.UserTestUtil.createTestUser;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import mate.academy.skillshare.dto.review.CreateReviewRequestDto;
import mate.academy.skillshare.dto.review.ReviewDto;
import mate.academy.skillshare.model.Review;

public class ReviewTestUtil {
    public static Review createTestReview() {
        Review review = new Review();
        review.setId(1L);
        review.setCourse(createTestCourse());
        review.setUser(createTestUser());
        review.setRating(1);
        review.setComment("comment1");
        review.setTimestamp(LocalDate.of(2024, 10, 31));
        return review;
    }

    public static ReviewDto createTestReviewDto(Review review) {
        return new ReviewDto(
                review.getId(),
                review.getUser().getId(),
                review.getRating(),
                review.getComment(),
                review.getTimestamp());
    }

    public static CreateReviewRequestDto createTestCreateReviewRequestDto(Review review) {
        return new CreateReviewRequestDto(
                review.getRating(),
                review.getComment()
        );
    }

    public static List<ReviewDto> fillExpectedReviewDtoList() {
        List<ReviewDto> reviewList = new ArrayList<>();
        reviewList.add(new ReviewDto(1L, 1L, 1, "comment1",
                LocalDate.of(2024, 10, 31)));
        reviewList.add(new ReviewDto(2L, 1L, 2, "comment2",
                LocalDate.of(2024, 10, 31)));
        return reviewList;
    }
}
