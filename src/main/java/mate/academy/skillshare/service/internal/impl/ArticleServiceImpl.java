package mate.academy.skillshare.service.internal.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.skillshare.dto.article.ArticleDto;
import mate.academy.skillshare.dto.article.CreateArticleRequestDto;
import mate.academy.skillshare.exception.EntityNotFoundException;
import mate.academy.skillshare.mapper.ArticleMapper;
import mate.academy.skillshare.model.Article;
import mate.academy.skillshare.repository.article.ArticleRepository;
import mate.academy.skillshare.service.internal.ArticleService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {
    private final ArticleRepository articleRepository;
    private final ArticleMapper articleMapper;

    @Override
    public ArticleDto create(CreateArticleRequestDto requestDto) {
        Article article = articleMapper.toModel(requestDto);
        return articleMapper.toDto(articleRepository.save(article));
    }

    @Override
    public List<ArticleDto> getAll(Pageable pageable) {
        return articleMapper.toDtoList(articleRepository.findAll(pageable).getContent());
    }

    @Override
    public ArticleDto get(Long id) {
        return articleMapper.toDto(articleRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Can't find article by id: " + id)));
    }

    @Override
    public ArticleDto update(Long id, CreateArticleRequestDto requestDto) {
        Article existingArticle = articleRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Can't find article by id: " + id));
        Article updatedArticle = articleMapper.updateFromRequest(requestDto, existingArticle);
        return articleMapper.toDto(articleRepository.save(updatedArticle));
    }

    @Override
    public void delete(Long id) {
        articleRepository.deleteById(id);
    }
}
