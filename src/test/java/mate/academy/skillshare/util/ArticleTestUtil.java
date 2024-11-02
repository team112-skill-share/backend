package mate.academy.skillshare.util;

import static mate.academy.skillshare.util.SubtitleTestUtil.createTestCreateSubtitleRequestDto;
import static mate.academy.skillshare.util.SubtitleTestUtil.createTestSubtitle;
import static mate.academy.skillshare.util.SubtitleTestUtil.createTestSubtitleDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import mate.academy.skillshare.dto.article.ArticleCardDto;
import mate.academy.skillshare.dto.article.ArticleDto;
import mate.academy.skillshare.dto.article.CreateArticleRequestDto;
import mate.academy.skillshare.model.Article;

public class ArticleTestUtil {
    public static Article createTestArticle() {
        Article article = new Article();
        article.setId(1L);
        article.setAuthor("author");
        article.setTitle("title");
        article.setCardImage("image");
        article.setDescription("description");
        article.setSubtitles(Set.of(createTestSubtitle()));
        return article;
    }

    public static ArticleCardDto createTestArticleCardDto(Article article) {
        return new ArticleCardDto(
                article.getId(),
                article.getAuthor(),
                article.getTitle(),
                article.getCardImage(),
                article.getDescription()
        );
    }

    public static ArticleDto createTestArticleDto(Article article) {
        return new ArticleDto(
                article.getId(),
                article.getAuthor(),
                article.getTitle(),
                article.getDescription(),
                article.getSource(),
                Set.of(createTestSubtitleDto(createTestSubtitle())),
                null,
                null,
                null
        );
    }

    public static CreateArticleRequestDto createTestCreateArticleRequestDto(Article article) {
        return new CreateArticleRequestDto(
                article.getAuthor(),
                article.getTitle(),
                article.getCardImage(),
                article.getDescription(),
                article.getSource(),
                List.of(createTestCreateSubtitleRequestDto(createTestSubtitle())),
                null,
                null
        );
    }

    public static List<ArticleCardDto> fillExpectedArticleCardDtoList() {
        List<ArticleCardDto> articleList = new ArrayList<>();
        articleList.add(new ArticleCardDto(1L, "author1", "title1", "image1", "description1"));
        articleList.add(new ArticleCardDto(2L, "author2", "title2", "image2", "description2"));
        return articleList;
    }
}
