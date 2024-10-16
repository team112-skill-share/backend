package mate.academy.skillshare.dto.course;

import java.math.BigDecimal;
import java.util.List;
import mate.academy.skillshare.dto.review.ReviewDto;

public record CourseDto(
        Long id,
        String author,
        String title,
        String duration,
        String format,
        boolean certificate,
        BigDecimal price,
        Long categoryId,
        String content,
        String source,
        List<ReviewDto> reviews
) {
}
