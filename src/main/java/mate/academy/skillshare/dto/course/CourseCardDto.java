package mate.academy.skillshare.dto.course;

import java.math.BigDecimal;

public record CourseCardDto(
        Long id,
        String author,
        String title,
        String cardImage,
        String duration,
        String type,
        String format,
        boolean certificate,
        boolean trial,
        BigDecimal price,
        Long categoryId,
        String source,
        Double averageRating,
        Integer reviewsCount
) {
}
