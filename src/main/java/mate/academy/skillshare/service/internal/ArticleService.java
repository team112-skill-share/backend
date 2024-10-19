package mate.academy.skillshare.service.internal;

import java.util.List;
import mate.academy.skillshare.dto.article.ArticleDto;
import mate.academy.skillshare.dto.article.CreateArticleRequestDto;
import org.springframework.data.domain.Pageable;

public interface ArticleService {
    ArticleDto create(CreateArticleRequestDto requestDto);

    List<ArticleDto> getAll(Pageable pageable);

    ArticleDto get(Long id);

    ArticleDto update(Long id, CreateArticleRequestDto requestDto);

    void delete(Long id);
}
