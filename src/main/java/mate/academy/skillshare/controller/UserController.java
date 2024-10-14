package mate.academy.skillshare.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mate.academy.skillshare.dto.user.UserEmailChangeRequestDto;
import mate.academy.skillshare.dto.user.UserInfoRequestDto;
import mate.academy.skillshare.dto.user.UserPasswordChangeRequestDto;
import mate.academy.skillshare.dto.user.UserResponseDto;
import mate.academy.skillshare.model.User;
import mate.academy.skillshare.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User managing", description = "Endpoints for managing users")
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Operation(summary = "Get user info", description = "Get current user's profile info")
    @GetMapping("/me")
    public UserResponseDto getUserInfo(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return userService.get(user.getId());
    }

    @Operation(summary = "Update user info", description = "Update current user's profile info")
    @PutMapping("/me")
    public UserResponseDto updateUserInfo(
            Authentication authentication,
            @RequestBody @Valid UserInfoRequestDto requestDto
    ) {
        User user = (User) authentication.getPrincipal();
        return userService.updateInfo(user.getId(), requestDto);
    }

    @Operation(summary = "Update user password", description = "Update current user's password")
    @PatchMapping("/me/password")
    public UserResponseDto updateUserPassword(
            Authentication authentication,
            @RequestBody @Valid UserPasswordChangeRequestDto requestDto
    ) {
        User user = (User) authentication.getPrincipal();
        return userService.updatePassword(user.getId(), requestDto);
    }

    @Operation(summary = "Update user email", description = "Update current user's email")
    @PatchMapping("/me/email")
    public UserResponseDto updateUserEmail(
            Authentication authentication,
            @RequestBody @Valid UserEmailChangeRequestDto requestDto
    ) {
        User user = (User) authentication.getPrincipal();
        return userService.updateEmail(user.getId(), requestDto);
    }

    @Operation(summary = "Add a course to favourites",
            description = "Add a course to user's favourites")
    @PostMapping("/me/favourites/{courseId}")
    public UserResponseDto addFavourite(
            Authentication authentication,
            @PathVariable Long courseId
    ) {
        User user = (User) authentication.getPrincipal();
        return userService.addFavouriteCourse(user.getId(), courseId);
    }

    @Operation(summary = "Remove a course from favourites",
            description = "Remove a course from user's favourites")
    @DeleteMapping("/me/favourites/{courseId}")
    public UserResponseDto removeFavourite(
            Authentication authentication,
            @PathVariable Long courseId
    ) {
        User user = (User) authentication.getPrincipal();
        return userService.removeFavouriteCourse(user.getId(), courseId);
    }
}
