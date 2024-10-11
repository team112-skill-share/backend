package mate.academy.skillshare.dto.comment;

import java.time.LocalDateTime;

public record CommentDto(
        Long id,
        Long articleId,
        Long userId,
        String comment,
        LocalDateTime timestamp
) {
}