package mate.academy.skillshare.dto.review;

import java.time.LocalDate;

public record ReviewDto(
        Long id,
        Long courseId,
        Long userId,
        Integer rating,
        String comment,
        LocalDate timestamp
) {
}
