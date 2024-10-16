package mate.academy.skillshare.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import mate.academy.skillshare.annotation.FieldMatch;
import mate.academy.skillshare.annotation.Password;
import org.hibernate.validator.constraints.Length;

@FieldMatch(firstField = "password", secondField = "repeatPassword",
        message = "Passwords do not match")
public record UserRegistrationRequestDto(
        @Email @NotBlank String email,
        @NotBlank @Length(min = 8) @Password String password,
        @NotBlank @Length(min = 8) @Password String repeatPassword
) {
}
