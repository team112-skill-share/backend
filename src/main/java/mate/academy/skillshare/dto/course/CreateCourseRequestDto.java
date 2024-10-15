package mate.academy.skillshare.dto.course;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import mate.academy.skillshare.model.Course;

public record CreateCourseRequestDto(
        @NotBlank String author,
        @NotBlank String title,
        @NotBlank String duration,
        @NotNull Course.Format format,
        @NotNull boolean certificate,
        @PositiveOrZero @NotNull BigDecimal price,
        @Positive @NotNull Long categoryId,
        @NotBlank String content,
        @NotBlank String source
) {
}
