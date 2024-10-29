package mate.academy.skillshare.dto.course;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import mate.academy.skillshare.dto.content.ContentDto;
import mate.academy.skillshare.dto.image.ImageDto;
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
        String source,
        Set<ContentDto> contents,
        Set<ImageDto> images,
        List<ReviewDto> reviews
) {
}
