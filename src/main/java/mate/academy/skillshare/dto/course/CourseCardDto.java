package mate.academy.skillshare.dto.course;

import java.math.BigDecimal;

public record CourseCardDto(
        Long id,
        String author,
        String title,
        String duration,
        String format,
        boolean certificate,
        BigDecimal price,
        Long categoryId,
        String source,
        Double averageRating,
        Integer reviewsCount
) {
}
