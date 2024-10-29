package mate.academy.skillshare.dto.review;

import java.time.LocalDate;

public record ReviewDto(
        Long id,
        Long userId,
        Integer rating,
        String comment,
        LocalDate timestamp
) {
}
