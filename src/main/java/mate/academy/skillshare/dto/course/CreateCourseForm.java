package mate.academy.skillshare.dto.course;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateCourseForm(
        @NotBlank String company,
        @NotBlank String phoneNumber,
        @NotBlank @Email String email,
        @NotBlank String workField,
        String description
) {
}
