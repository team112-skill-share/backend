package mate.academy.skillshare.dto.article;

public record ArticleCardDto(
        Long id,
        String author,
        String title,
        String cardImage,
        String description
) {
}
