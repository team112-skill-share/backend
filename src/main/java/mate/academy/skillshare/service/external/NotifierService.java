package mate.academy.skillshare.service.external;

import lombok.RequiredArgsConstructor;
import mate.academy.skillshare.dto.course.CreateCourseForm;
import mate.academy.skillshare.dto.user.UserForgotPasswordRequestDto;
import mate.academy.skillshare.exception.EntityNotFoundException;
import mate.academy.skillshare.repository.user.UserRepository;
import mate.academy.skillshare.security.internal.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotifierService {
    private static final String passwordResetLink
            = "https://skill-share-112.netlify.app/reset-password?token="; //placeholder!
    private final EmailService emailService;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    @Value("${course.email}")
    private String newCourseEmail;

    public void sendResetPasswordLink(UserForgotPasswordRequestDto requestDto) {
        if (userRepository.findByEmail(requestDto.email()).isEmpty()) {
            throw new EntityNotFoundException("User with such email does not exist");
        }
        String resetLink = passwordResetLink
                + jwtUtil.generatePasswordResetToken(requestDto.email());
        String message = "We received a request to reset your password, "
                + "if it was you - click the link below" + System.lineSeparator()
                + resetLink + System.lineSeparator()
                + "If you didn't make such request - just ignore this email";
        emailService.sendEmail(requestDto.email(), "Password reset", message);
    }

    public void notifyAboutCourseRequest(CreateCourseForm requestDto) {
        StringBuilder message = new StringBuilder();
        message.append("Company: ").append(requestDto.company())
                .append(System.lineSeparator()).append("Phone number: ")
                .append(requestDto.phoneNumber()).append(System.lineSeparator())
                .append("Contact email: ").append(requestDto.email())
                .append(System.lineSeparator()).append("Field: ")
                .append(requestDto.workField());
        if (requestDto.description() != null) {
            message.append(System.lineSeparator()).append("Description: ")
                    .append(requestDto.description());
        }
        emailService.sendEmail(newCourseEmail, "New course request", message.toString());
    }
}
