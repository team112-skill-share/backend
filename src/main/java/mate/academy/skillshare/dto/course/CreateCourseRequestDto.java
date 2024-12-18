package mate.academy.skillshare.dto.course;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.util.List;
import mate.academy.skillshare.dto.content.CreateContentRequestDto;
import mate.academy.skillshare.dto.image.CreateImageRequestDto;
import mate.academy.skillshare.model.Course;

public record CreateCourseRequestDto(
        @NotBlank String author,
        @NotBlank String title,
        String cardImage,
        @NotBlank String duration,
        @NotNull Course.CourseType type,
        @NotNull Course.Format format,
        @NotNull boolean certificate,
        @NotNull boolean trial,
        @PositiveOrZero @NotNull BigDecimal price,
        @Positive @NotNull Long categoryId,
        String source,
        List<CreateContentRequestDto> contents,
        List<CreateImageRequestDto> images
) {
}
