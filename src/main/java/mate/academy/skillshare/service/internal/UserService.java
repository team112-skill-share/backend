package mate.academy.skillshare.service.internal;

import mate.academy.skillshare.dto.user.GoogleUserDto;
import mate.academy.skillshare.dto.user.UserEmailChangeRequestDto;
import mate.academy.skillshare.dto.user.UserInfoRequestDto;
import mate.academy.skillshare.dto.user.UserPasswordChangeRequestDto;
import mate.academy.skillshare.dto.user.UserRegistrationRequestDto;
import mate.academy.skillshare.dto.user.UserResetPasswordRequestDto;
import mate.academy.skillshare.dto.user.UserResponseDto;
import mate.academy.skillshare.exception.RegistrationException;
import mate.academy.skillshare.model.User;

public interface UserService {
    UserResponseDto register(UserRegistrationRequestDto requestDto)
            throws RegistrationException;

    UserResponseDto get(Long id);

    UserResponseDto updateInfo(Long id, UserInfoRequestDto requestDto);

    UserResponseDto updatePassword(Long id, UserPasswordChangeRequestDto requestDto);

    UserResponseDto updateEmail(Long id, UserEmailChangeRequestDto requestDto);

    UserResponseDto addFavouriteCourse(Long userId, Long courseId);

    UserResponseDto removeFavouriteCourse(Long userId, Long courseId);

    User registerGoogleUser(GoogleUserDto googleUser);

    void resetPassword(UserResetPasswordRequestDto requestDto);
}
