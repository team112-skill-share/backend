package mate.academy.skillshare.util;

import static mate.academy.skillshare.util.CourseTestUtil.createTestCourse;
import static mate.academy.skillshare.util.CourseTestUtil.createTestCourseCardDto;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Set;
import mate.academy.skillshare.dto.user.GoogleUserDto;
import mate.academy.skillshare.dto.user.UserEmailChangeRequestDto;
import mate.academy.skillshare.dto.user.UserInfoRequestDto;
import mate.academy.skillshare.dto.user.UserPasswordChangeRequestDto;
import mate.academy.skillshare.dto.user.UserRegistrationRequestDto;
import mate.academy.skillshare.dto.user.UserResponseDto;
import mate.academy.skillshare.model.Role;
import mate.academy.skillshare.model.User;

public class UserTestUtil {
    public static User createTestUser() {
        User user = new User();
        user.setId(1L);
        user.setEmail("bob@example.test");
        user.setPassword("Bobster1");
        user.setRoles(Set.of(createUserRole()));
        return user;
    }

    public static Role createUserRole() {
        Role role = new Role();
        role.setId(1L);
        role.setRoleName(Role.RoleName.ROLE_USER);
        return role;
    }

    public static User updateTestUser(User user) {
        user.setFullName("Bob");
        user.setBirthDate(LocalDate.of(2000, 1, 1));
        user.setPhoneNumber("+380000000000");
        return user;
    }

    public static UserResponseDto createTestUserResponseDto(User user) {
        return new UserResponseDto(
                user.getId(),
                user.getEmail(),
                user.getFullName(),
                user.getBirthDate(),
                user.getPhoneNumber(),
                Collections.emptySet()
        );
    }

    public static UserResponseDto createTestUserResponseDtoWithCourse(User user) {
        return new UserResponseDto(
                user.getId(),
                user.getEmail(),
                user.getFullName(),
                user.getBirthDate(),
                user.getPhoneNumber(),
                Set.of(createTestCourseCardDto(createTestCourse()))
        );
    }

    public static UserRegistrationRequestDto createTestUserRegistrationRequestDto(User user) {
        return new UserRegistrationRequestDto(
                user.getEmail(),
                user.getPassword(),
                user.getPassword()
        );
    }

    public static UserInfoRequestDto createTestUserInfoRequestDto(User user) {
        return new UserInfoRequestDto(
                user.getFullName(),
                user.getBirthDate(),
                user.getPhoneNumber()
        );
    }

    public static UserPasswordChangeRequestDto createTestUserPasswordChangeRequestDto(User user) {
        return new UserPasswordChangeRequestDto(
                user.getPassword(),
                "NEWpasswor3",
                "NEWpasswor3"
        );
    }

    public static UserEmailChangeRequestDto createTestUserEmailChangeRequestDto() {
        return new UserEmailChangeRequestDto(
                "newbob@example.test"
        );
    }

    public static GoogleUserDto createTestGoogleUserDto(User user) {
        return new GoogleUserDto(
                user.getEmail(),
                user.getFullName()
        );
    }
}
