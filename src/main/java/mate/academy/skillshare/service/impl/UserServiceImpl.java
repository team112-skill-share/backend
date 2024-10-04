package mate.academy.skillshare.service.impl;

import java.util.Set;
import lombok.RequiredArgsConstructor;
import mate.academy.skillshare.dto.user.UserInfoRequestDto;
import mate.academy.skillshare.dto.user.UserRegistrationRequestDto;
import mate.academy.skillshare.dto.user.UserResponseDto;
import mate.academy.skillshare.exception.EntityNotFoundException;
import mate.academy.skillshare.exception.RegistrationException;
import mate.academy.skillshare.mapper.UserMapper;
import mate.academy.skillshare.model.Role;
import mate.academy.skillshare.model.User;
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

    @Override
    public UserResponseDto register(UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        if (userRepository.findByEmail(requestDto.getEmail()).isPresent()) {
            throw new RegistrationException("Can't register new user. "
                    + "User with this email already exists");
        }
        User user = userMapper.toModel(requestDto);
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
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
}
