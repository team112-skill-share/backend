package mate.academy.skillshare.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserForgotPasswordRequestDto(
        @NotBlank @Email String email
) {
}
