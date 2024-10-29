package mate.academy.skillshare.dto.article;

import jakarta.validation.constraints.NotBlank;
import java.util.List;
import mate.academy.skillshare.dto.content.CreateContentRequestDto;
import mate.academy.skillshare.dto.subtitle.CreateSubtitleRequestDto;
import org.springframework.web.multipart.MultipartFile;

public record CreateArticleRequestDto(
        @NotBlank String author,
        @NotBlank String title,
        @NotBlank String description,
        String source,
        List<CreateSubtitleRequestDto> subtitles,
        List<CreateContentRequestDto> contents,
        List<MultipartFile> images
) {
}
