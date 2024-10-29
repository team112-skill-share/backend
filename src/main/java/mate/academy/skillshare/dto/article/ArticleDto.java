package mate.academy.skillshare.dto.article;

import java.util.List;
import java.util.Set;
import mate.academy.skillshare.dto.comment.CommentDto;
import mate.academy.skillshare.dto.content.ContentDto;
import mate.academy.skillshare.dto.image.ImageDto;
import mate.academy.skillshare.dto.subtitle.SubtitleDto;

public record ArticleDto(
        Long id,
        String author,
        String title,
        String description,
        String source,
        Set<SubtitleDto> subtitles,
        Set<ContentDto> contents,
        Set<ImageDto> images,
        List<CommentDto> comments
) {
}
