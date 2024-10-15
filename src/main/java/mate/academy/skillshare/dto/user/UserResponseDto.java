package mate.academy.skillshare.dto.user;

import java.time.LocalDate;
import java.util.Set;
import mate.academy.skillshare.dto.course.CourseDto;

public record UserResponseDto(
        Long id,
        String email,
        String fullName,
        LocalDate birthDate,
        String phoneNumber,
        Set<CourseDto> favourites
) {
}
