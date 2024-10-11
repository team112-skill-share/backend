package mate.academy.skillshare.service;

import java.util.List;
import mate.academy.skillshare.dto.article.CreateArticleRequestDto;
import mate.academy.skillshare.model.Article;
import org.springframework.data.domain.Pageable;

public interface ArticleService {
    Article create(CreateArticleRequestDto requestDto);

    List<Article> getAll(Pageable pageable);

    Article get(Long id);

    Article update(Long id, CreateArticleRequestDto requestDto);

    void delete(Long id);
}
