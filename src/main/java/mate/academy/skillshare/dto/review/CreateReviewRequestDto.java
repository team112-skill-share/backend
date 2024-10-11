package mate.academy.skillshare.dto.review;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateReviewRequestDto(
        @Positive @NotNull Integer rating,
        String comment
) {
}
