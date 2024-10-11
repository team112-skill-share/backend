package mate.academy.skillshare.dto.user;

import jakarta.validation.constraints.NotBlank;
import mate.academy.skillshare.annotation.FieldMatch;
import mate.academy.skillshare.annotation.Password;
import org.hibernate.validator.constraints.Length;

@FieldMatch(firstField = "newPassword", secondField = "repeatPassword",
        message = "Passwords do not match")
public record UserPasswordChangeRequestDto(
        @NotBlank String currentPassword,
        @NotBlank @Length(min = 8) @Password String newPassword,
        @NotBlank @Length(min = 8) @Password String repeatPassword
) {
}
