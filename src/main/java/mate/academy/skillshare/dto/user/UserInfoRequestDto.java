package mate.academy.skillshare.dto.user;

import java.time.LocalDate;

public record UserInfoRequestDto(
        String fullName,
        LocalDate birthDate,
        String phoneNumber
) {
}
