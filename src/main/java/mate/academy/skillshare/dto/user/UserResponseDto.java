package mate.academy.skillshare.dto.user;

import java.time.LocalDate;

public record UserResponseDto(
        Long id,
        String email,
        String fullName,
        LocalDate birthDate,
        String phoneNumber
) {
}
