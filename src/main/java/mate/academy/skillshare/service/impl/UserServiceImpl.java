package mate.academy.skillshare.service.impl;

import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import mate.academy.skillshare.dto.user.GoogleUserDto;
import mate.academy.skillshare.dto.user.UserEmailChangeRequestDto;
import mate.academy.skillshare.dto.user.UserInfoRequestDto;
import mate.academy.skillshare.dto.user.UserPasswordChangeRequestDto;
import mate.academy.skillshare.dto.user.UserRegistrationRequestDto;
import mate.academy.skillshare.dto.user.UserResponseDto;
import mate.academy.skillshare.exception.EntityNotFoundException;
import mate.academy.skillshare.exception.RegistrationException;
import mate.academy.skillshare.mapper.UserMapper;
import mate.academy.skillshare.model.Course;
import mate.academy.skillshare.model.Role;
import mate.academy.skillshare.model.User;
import mate.academy.skillshare.repository.course.CourseRepository;
import mate.academy.skillshare.repository.role.RoleRepository;
import mate.academy.skillshare.repository.user.UserRepository;
import mate.academy.skillshare.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final CourseRepository courseRepository;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        if (userRepository.findByEmail(requestDto.email()).isPresent()) {
            throw new RegistrationException("Can't register new user. "
                    + "User with this email already exists");
        }
        User user = userMapper.toModel(requestDto);
        user.setPassword(passwordEncoder.encode(requestDto.password()));
        Role role = roleRepository.findByRoleName(Role.RoleName.ROLE_USER);
        user.setRoles(Set.of(role));
        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public UserResponseDto get(Long id) {
        return userMapper.toDto(userRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Can't find user by id: " + id)));
    }

    @Override
    public UserResponseDto updateInfo(Long id, UserInfoRequestDto requestDto) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Can't find user by id: " + id));
        User updatedUser = userMapper.updateInfo(user, requestDto);
        return userMapper.toDto(userRepository.save(updatedUser));
    }

    @Override
    public UserResponseDto updatePassword(Long id, UserPasswordChangeRequestDto requestDto) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Can't find user by id: " + id));
        if (!passwordEncoder.matches(requestDto.currentPassword(), user.getPassword())) {
            throw new RuntimeException("Current password is incorrect");
        }
        user.setPassword(passwordEncoder.encode(requestDto.newPassword()));
        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public UserResponseDto updateEmail(Long id, UserEmailChangeRequestDto requestDto) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Can't find user by id: " + id));
        String currentEmail = user.getEmail();
        String newEmail = requestDto.email();
        if (currentEmail.equals(newEmail)) {
            return userMapper.toDto(user);
        }
        if (userRepository.findByEmail(newEmail).isPresent()) {
            throw new RuntimeException("User with this email already exists");
        }
        user.setEmail(newEmail);
        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public UserResponseDto addFavouriteCourse(Long userId, Long courseId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new EntityNotFoundException("Can't find user by id: " + userId));
        Course course = courseRepository.findById(courseId).orElseThrow(() ->
                new EntityNotFoundException("Can't find course by id: " + courseId));
        user.getFavourites().add(course);
        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public UserResponseDto removeFavouriteCourse(Long userId, Long courseId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new EntityNotFoundException("Can't find user by id: " + userId));
        Course course = courseRepository.findById(courseId).orElseThrow(() ->
                new EntityNotFoundException("Can't find course by id: " + courseId));
        user.getFavourites().remove(course);
        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public User registerGoogleUser(GoogleUserDto googleUser) {
        User user = userMapper.toModel(googleUser);
        user.setPassword(generateDummyPassword());
        Role role = roleRepository.findByRoleName(Role.RoleName.ROLE_USER);
        user.setRoles(Set.of(role));
        user.setGoogleAccount(true);
        return userRepository.save(user);
    }

    private String generateDummyPassword() {
        return UUID.randomUUID().toString();
    }
}
