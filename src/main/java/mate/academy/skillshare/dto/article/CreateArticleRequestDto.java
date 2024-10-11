package mate.academy.skillshare.dto.article;

import jakarta.validation.constraints.NotBlank;

public record CreateArticleRequestDto(
        @NotBlank String author,
        @NotBlank String title,
        @NotBlank String content,
        String source
) {
}
