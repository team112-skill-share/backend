package mate.academy.skillshare.mapper;

import mate.academy.skillshare.config.MapperConfig;
import mate.academy.skillshare.dto.user.GoogleUserDto;
import mate.academy.skillshare.dto.user.UserInfoRequestDto;
import mate.academy.skillshare.dto.user.UserRegistrationRequestDto;
import mate.academy.skillshare.dto.user.UserResponseDto;
import mate.academy.skillshare.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class, uses = CourseMapper.class)
public interface UserMapper {
    UserResponseDto toDto(User user);

    User toModel(UserRegistrationRequestDto requestDto);

    User toModel(GoogleUserDto googleUser);

    User updateInfo(@MappingTarget User user, UserInfoRequestDto requestDto);
}
