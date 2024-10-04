package mate.academy.skillshare.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import mate.academy.skillshare.annotation.FieldMatch;
import org.hibernate.validator.constraints.Length;

@Data
@FieldMatch(firstField = "password", secondField = "repeatPassword",
        message = "Passwords do not match")
public class UserRegistrationRequestDto {
    @Email
    @NotBlank
    private String email;
    @NotBlank
    @Length(min = 6)
    private String password;
    @NotBlank
    @Length(min = 6)
    private String repeatPassword;
}
