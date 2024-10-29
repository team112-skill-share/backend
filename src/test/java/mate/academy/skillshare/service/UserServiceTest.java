package mate.academy.skillshare.service;

import static mate.academy.skillshare.util.CourseTestUtil.createTestCourse;
import static mate.academy.skillshare.util.CourseTestUtil.createTestCourseCardDto;
import static mate.academy.skillshare.util.UserTestUtil.createTestGoogleUserDto;
import static mate.academy.skillshare.util.UserTestUtil.createTestUser;
import static mate.academy.skillshare.util.UserTestUtil.createTestUserEmailChangeRequestDto;
import static mate.academy.skillshare.util.UserTestUtil.createTestUserInfoRequestDto;
import static mate.academy.skillshare.util.UserTestUtil.createTestUserPasswordChangeRequestDto;
import static mate.academy.skillshare.util.UserTestUtil.createTestUserRegistrationRequestDto;
import static mate.academy.skillshare.util.UserTestUtil.createTestUserResponseDto;
import static mate.academy.skillshare.util.UserTestUtil.createTestUserResponseDtoWithCourse;
import static mate.academy.skillshare.util.UserTestUtil.updateTestUser;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import jakarta.persistence.EntityExistsException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import mate.academy.skillshare.dto.course.CourseCardDto;
import mate.academy.skillshare.dto.user.GoogleUserDto;
import mate.academy.skillshare.dto.user.UserEmailChangeRequestDto;
import mate.academy.skillshare.dto.user.UserInfoRequestDto;
import mate.academy.skillshare.dto.user.UserPasswordChangeRequestDto;
import mate.academy.skillshare.dto.user.UserRegistrationRequestDto;
import mate.academy.skillshare.dto.user.UserResponseDto;
import mate.academy.skillshare.exception.EntityNotFoundException;
import mate.academy.skillshare.exception.InvalidDataException;
import mate.academy.skillshare.exception.RegistrationException;
import mate.academy.skillshare.mapper.UserMapper;
import mate.academy.skillshare.model.Course;
import mate.academy.skillshare.model.Role;
import mate.academy.skillshare.model.User;
import mate.academy.skillshare.repository.course.CourseRepository;
import mate.academy.skillshare.repository.role.RoleRepository;
import mate.academy.skillshare.repository.user.UserRepository;
import mate.academy.skillshare.service.internal.impl.UserServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    @DisplayName("Verify that correct UserResponseDto was returned when calling register() method")
    public void register_WithValidRequestDto_ShouldReturnValidUserResponseDto()
            throws RegistrationException {
        //Given
        User user = createTestUser();
        UserRegistrationRequestDto requestDto = createTestUserRegistrationRequestDto(user);
        final UserResponseDto expected = createTestUserResponseDto(user);
        final Role role = user.getRoles().iterator().next();
        user.setPassword("encodedPassword");

        when(userRepository.findByEmail(requestDto.email())).thenReturn(Optional.empty());
        when(userMapper.toModel(requestDto)).thenReturn(user);
        when(passwordEncoder.encode(requestDto.password())).thenReturn("encodedPassword");
        when(roleRepository.findByRoleName(Role.RoleName.ROLE_USER)).thenReturn(role);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(expected);
        //When
        UserResponseDto actual = userService.register(requestDto);
        //Then
        assertEquals(expected, actual);

        verify(userRepository, times(1)).findByEmail(requestDto.email());
        verify(userMapper, times(1)).toModel(requestDto);
        verify(passwordEncoder, times(1)).encode(requestDto.password());
        verify(roleRepository, times(1)).findByRoleName(Role.RoleName.ROLE_USER);
        verify(userRepository, times(1)).save(user);
        verify(userMapper, times(1)).toDto(user);
        verifyNoMoreInteractions(userRepository, userMapper, passwordEncoder, roleRepository);
    }

    @Test
    @DisplayName("""
            Verify that exception is thrown when calling register() method
            and user with such email already exists""")
    public void register_WithExistingEmail_ShouldThrowException() {
        //Given
        User existingUser = createTestUser();
        UserRegistrationRequestDto requestDto = createTestUserRegistrationRequestDto(existingUser);

        when(userRepository.findByEmail(requestDto.email()))
                .thenReturn(Optional.of(existingUser));
        //When
        Exception exception = assertThrows(
                RegistrationException.class,
                () -> userService.register(requestDto)
        );
        //Then
        String expected = "Can't register new user. User with this email already exists";
        String actual = exception.getMessage();
        assertEquals(expected, actual);

        verify(userRepository, times(1)).findByEmail(requestDto.email());
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    @DisplayName("Verify that correct UserResponseDto was returned when calling get() method")
    public void get_WithValidId_ShouldReturnValidUserResponseDto() {
        //Given
        User user = createTestUser();
        UserResponseDto expected = createTestUserResponseDto(user);
        Long id = expected.id();

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(expected);
        //When
        UserResponseDto actual = userService.get(id);
        //Then
        assertEquals(expected, actual);

        verify(userRepository, times(1)).findById(id);
        verify(userMapper, times(1)).toDto(user);
        verifyNoMoreInteractions(userRepository, userMapper);
    }

    @Test
    @DisplayName("Verify that exception is thrown when calling get() method with invalid id")
    public void get_WithInvalidId_ShouldThrowException() {
        //Given
        Long id = 2L;

        when(userRepository.findById(id)).thenReturn(Optional.empty());
        //When
        Exception exception = assertThrows(
                EntityNotFoundException.class,
                () -> userService.get(id)
        );
        //Then
        String expected = "Can't find user by id: " + id;
        String actual = exception.getMessage();
        assertEquals(expected, actual);

        verify(userRepository, times(1)).findById(id);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    @DisplayName("""
            Verify that correct UserResponseDto was returned when calling updateInfo() method""")
    public void updateInfo_WithValidRequestDto_ShouldReturnValidUserResponseDto() {
        //Given
        User user = createTestUser();
        User updatedUser = updateTestUser(user);
        UserInfoRequestDto requestDto = createTestUserInfoRequestDto(user);
        UserResponseDto expected = createTestUserResponseDto(updatedUser);
        Long id = expected.id();

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userMapper.updateInfo(user, requestDto)).thenReturn(updatedUser);
        when(userRepository.save(updatedUser)).thenReturn(updatedUser);
        when(userMapper.toDto(updatedUser)).thenReturn(expected);
        //When
        UserResponseDto actual = userService.updateInfo(id, requestDto);
        //Then
        assertEquals(expected, actual);

        verify(userRepository, times(1)).findById(id);
        verify(userMapper, times(1)).updateInfo(user, requestDto);
        verify(userRepository, times(1)).save(updatedUser);
        verify(userMapper, times(1)).toDto(updatedUser);
        verifyNoMoreInteractions(userRepository, userMapper);
    }

    @Test
    @DisplayName("""
            Verify that correct UserResponseDto was returned when calling
             updatePassword() method""")
    public void updatePassword_WithValidRequestDto_ShouldReturnValidUserResponseDto() {
        //Given
        User user = createTestUser();
        user.setPassword("encodedPassword");
        UserPasswordChangeRequestDto requestDto = createTestUserPasswordChangeRequestDto(user);
        UserResponseDto expected = createTestUserResponseDto(user);
        Long id = expected.id();

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(requestDto.currentPassword(), user.getPassword()))
                .thenReturn(true);
        when(passwordEncoder.encode(requestDto.newPassword()))
                .thenReturn(user.getPassword());
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(expected);
        //When
        UserResponseDto actual = userService.updatePassword(id, requestDto);
        //Then
        assertEquals(expected, actual);

        verify(userRepository, times(1)).findById(id);
        verify(passwordEncoder, times(1)).matches(requestDto.currentPassword(), user.getPassword());
        verify(passwordEncoder, times(1)).encode(requestDto.newPassword());
        verify(userRepository, times(1)).save(user);
        verify(userMapper, times(1)).toDto(user);
        verifyNoMoreInteractions(userRepository, passwordEncoder, userMapper);
    }

    @Test
    @DisplayName("""
            Verify that exception is thrown when calling
             updatePassword() method with invalid current password""")
    public void updatePassword_WithInvalidCurrentPassword_ShouldThrowException() {
        //Given
        User user = createTestUser();
        UserPasswordChangeRequestDto requestDto = createTestUserPasswordChangeRequestDto(user);
        user.setPassword("wrongPassword");
        Long id = user.getId();

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(requestDto.currentPassword(), user.getPassword()))
                .thenReturn(false);
        //When
        Exception exception = assertThrows(
                InvalidDataException.class,
                () -> userService.updatePassword(id, requestDto)
        );
        //Then
        String expected = "Current password is incorrect";
        String actual = exception.getMessage();
        assertEquals(expected, actual);

        verify(userRepository, times(1)).findById(id);
        verify(passwordEncoder, times(1)).matches(requestDto.currentPassword(), user.getPassword());
        verifyNoMoreInteractions(userRepository, passwordEncoder);
    }

    @Test
    @DisplayName("""
            Verify that correct UserResponseDto was returned when calling updateEmail() method""")
    public void updateEmail_WithValidRequestDto_ShouldReturnValidUserResponseDto() {
        //Given
        User user = createTestUser();
        UserEmailChangeRequestDto requestDto = createTestUserEmailChangeRequestDto();
        User updatedUser = createTestUser();
        updatedUser.setEmail(requestDto.email());
        UserResponseDto expected = createTestUserResponseDto(updatedUser);
        Long id = expected.id();

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userRepository.findByEmail(requestDto.email())).thenReturn(Optional.empty());
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(expected);
        //When
        UserResponseDto actual = userService.updateEmail(id, requestDto);
        //Then
        assertEquals(expected, actual);

        verify(userRepository, times(1)).findById(id);
        verify(userRepository, times(1)).findByEmail(requestDto.email());
        verify(userRepository, times(1)).save(user);
        verify(userMapper, times(1)).toDto(user);
        verifyNoMoreInteractions(userRepository, userMapper);
    }

    @Test
    @DisplayName("""
            Verify that correct UserResponseDto was returned when calling updateEmail() method""")
    public void updateEmail_WithSameEmail_ShouldReturnValidUserResponseDto() {
        //Given
        User user = createTestUser();
        UserEmailChangeRequestDto requestDto = new UserEmailChangeRequestDto(user.getEmail());
        UserResponseDto expected = createTestUserResponseDto(user);
        Long id = expected.id();

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(expected);
        //When
        UserResponseDto actual = userService.updateEmail(id, requestDto);
        //Then
        assertEquals(expected, actual);

        verify(userRepository, times(1)).findById(id);
        verify(userMapper, times(1)).toDto(user);
        verifyNoMoreInteractions(userRepository, userMapper);
    }

    @Test
    @DisplayName("""
            Verify that exception is thrown when calling
             updateEmail() method with existing user with this email""")
    public void updateEmail_WithExistingUser_ShouldThrowException() {
        //Given
        User user = createTestUser();
        UserEmailChangeRequestDto requestDto = createTestUserEmailChangeRequestDto();
        User updatedUser = createTestUser();
        updatedUser.setEmail(requestDto.email());
        Long id = user.getId();

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userRepository.findByEmail(requestDto.email())).thenReturn(Optional.of(updatedUser));
        //When
        Exception exception = assertThrows(
                InvalidDataException.class,
                () -> userService.updateEmail(id, requestDto)
        );
        //Then
        String expected = "User with this email already exists";
        String actual = exception.getMessage();
        assertEquals(expected, actual);

        verify(userRepository, times(1)).findById(id);
        verify(userRepository, times(1)).findByEmail(requestDto.email());
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    @DisplayName("Verify that correct User was returned when calling registerGoogleUser() method")
    public void registerGoogleUser_WithValidRequestDto_ShouldReturnValidUser()
            throws RegistrationException {
        //Given
        User expected = createTestUser();
        expected.setGoogleAccount(true);
        GoogleUserDto requestDto = createTestGoogleUserDto(expected);
        Role role = expected.getRoles().iterator().next();

        when(userMapper.toModel(requestDto)).thenReturn(expected);
        when(roleRepository.findByRoleName(Role.RoleName.ROLE_USER)).thenReturn(role);
        when(userRepository.save(expected)).thenReturn(expected);
        //When
        User actual = userService.registerGoogleUser(requestDto);
        //Then
        assertEquals(expected, actual);

        verify(userMapper, times(1)).toModel(requestDto);
        verify(roleRepository, times(1)).findByRoleName(Role.RoleName.ROLE_USER);
        verify(userRepository, times(1)).save(expected);
        verifyNoMoreInteractions(userMapper, roleRepository, userRepository);
    }

    @Test
    @DisplayName("""
            Verify that course was added to favourites when calling
             addFavouriteCourse() method""")
    public void addFavouriteCourse_WithValidId_ShouldReturnValidUserResponseDto() {
        //Given
        Course course = createTestCourse();
        CourseCardDto courseDto = createTestCourseCardDto(course);
        User user = createTestUser();
        UserResponseDto expected = createTestUserResponseDtoWithCourse(user);
        Long userId = expected.id();
        Long courseId = courseDto.id();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(expected);
        //When
        UserResponseDto actual = userService.addFavouriteCourse(userId, courseId);
        //Then
        assertEquals(expected, actual);

        verify(userRepository, times(1)).findById(userId);
        verify(courseRepository, times(1)).findById(courseId);
        verify(userRepository, times(1)).save(user);
        verify(userMapper, times(1)).toDto(user);
        verifyNoMoreInteractions(userRepository, courseRepository, userMapper);
    }

    @Test
    @DisplayName("""
            Verify that exception is thrown when calling
             addFavouriteCourse() method with invalid course id""")
    public void addFavouriteCourse_WithInvalidCourseId_ShouldThrowException() {
        //Given
        User user = createTestUser();
        Long courseId = 2L;

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());
        //When
        Exception exception = assertThrows(
                EntityNotFoundException.class,
                () -> userService.addFavouriteCourse(user.getId(), courseId)
        );
        //Then
        String expected = "Can't find course by id: " + courseId;
        String actual = exception.getMessage();
        assertEquals(expected, actual);

        verify(userRepository, times(1)).findById(user.getId());
        verify(courseRepository, times(1)).findById(courseId);
        verifyNoMoreInteractions(userRepository, courseRepository);
    }

    @Test
    @DisplayName("""
            Verify that exception is thrown when calling addFavouriteCourse() method
            with already added favourite course""")
    public void addFavouriteCourse_WithDuplicateCourse_ShouldThrowException() {
        //Given
        Course course = createTestCourse();
        CourseCardDto courseDto = createTestCourseCardDto(course);
        User user = createTestUser();
        user.setFavourites(new HashSet<>(Set.of(course)));

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(courseRepository.findById(courseDto.id())).thenReturn(Optional.of(course));
        //When
        Exception exception = assertThrows(
                EntityExistsException.class,
                () -> userService.addFavouriteCourse(user.getId(), course.getId())
        );
        //Then
        String expected = "Course " + course.getTitle() + " is already added into favourites";
        String actual = exception.getMessage();
        assertEquals(expected, actual);

        verify(userRepository, times(1)).findById(user.getId());
        verify(courseRepository, times(1)).findById(courseDto.id());
        verifyNoMoreInteractions(userRepository, courseRepository);
    }

    @Test
    @DisplayName("Verify that user was removed from the project when calling removeUser() method")
    public void removeUser_WithValidRequestDto_ShouldReturnValidProjectDto() {
        //Given
        Course course = createTestCourse();
        CourseCardDto courseDto = createTestCourseCardDto(course);
        User user = createTestUser();
        UserResponseDto expected = createTestUserResponseDto(user);
        Long userId = expected.id();
        Long courseId = courseDto.id();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(expected);
        //When
        UserResponseDto actual = userService.removeFavouriteCourse(userId, courseId);
        //Then
        assertEquals(expected, actual);

        verify(userRepository, times(1)).findById(userId);
        verify(courseRepository, times(1)).findById(courseId);
        verify(userRepository, times(1)).save(user);
        verify(userMapper, times(1)).toDto(user);
        verifyNoMoreInteractions(userRepository, courseRepository, userMapper);
    }
}
