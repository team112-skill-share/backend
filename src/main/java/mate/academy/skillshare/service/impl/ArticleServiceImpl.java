package mate.academy.skillshare.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.skillshare.dto.article.CreateArticleRequestDto;
import mate.academy.skillshare.exception.EntityNotFoundException;
import mate.academy.skillshare.mapper.ArticleMapper;
import mate.academy.skillshare.model.Article;
import mate.academy.skillshare.repository.article.ArticleRepository;
import mate.academy.skillshare.service.ArticleService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {
    private final ArticleRepository articleRepository;
    private final ArticleMapper articleMapper;

    @Override
    public Article create(CreateArticleRequestDto requestDto) {
        return articleRepository.save(articleMapper.toModel(requestDto));
    }

    @Override
    public List<Article> getAll(Pageable pageable) {
        return articleRepository.findAll(pageable).getContent();
    }

    @Override
    public Article get(Long id) {
        return articleRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Can't find article by id: " + id));
    }

    @Override
    public Article update(Long id, CreateArticleRequestDto requestDto) {
        Article existingArticle = articleRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Can't find article by id: " + id));
        Article updatedArticle = articleMapper.updateFromRequest(requestDto, existingArticle);
        return articleRepository.save(updatedArticle);
    }

    @Override
    public void delete(Long id) {
        articleRepository.deleteById(id);
    }
}
