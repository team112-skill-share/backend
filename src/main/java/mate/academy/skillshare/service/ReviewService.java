package mate.academy.skillshare.service;

import java.util.List;
import mate.academy.skillshare.dto.review.CreateReviewRequestDto;
import mate.academy.skillshare.dto.review.ReviewDto;
import org.springframework.data.domain.Pageable;

public interface ReviewService {
    ReviewDto create(Long userId, Long courseId, CreateReviewRequestDto requestDto);

    List<ReviewDto> getAll(Long courseId, Pageable pageable);

    void delete(Long id);
}
