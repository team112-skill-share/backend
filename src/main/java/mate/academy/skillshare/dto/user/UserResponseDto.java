package mate.academy.skillshare.dto.user;

import java.time.LocalDate;
import java.util.List;
import mate.academy.skillshare.dto.course.CourseDto;

public record UserResponseDto(
        Long id,
        String email,
        String fullName,
        LocalDate birthDate,
        String phoneNumber,
        List<CourseDto> favourites
) {
}
