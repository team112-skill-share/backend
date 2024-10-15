package mate.academy.skillshare.dto.article;

import java.util.List;
import mate.academy.skillshare.dto.comment.CommentDto;

public record ArticleDto(
        Long id,
        String author,
        String title,
        String content,
        String source,
        List<CommentDto> comments
) {
}
