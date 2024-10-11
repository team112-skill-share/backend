package mate.academy.skillshare.dto.comment;

import jakarta.validation.constraints.NotBlank;

public record CreateCommentRequestDto(
        @NotBlank String comment
) {
}
