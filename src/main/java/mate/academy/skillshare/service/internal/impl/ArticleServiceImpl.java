package mate.academy.skillshare.service.internal.impl;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import mate.academy.skillshare.dto.article.ArticleCardDto;
import mate.academy.skillshare.dto.article.ArticleDto;
import mate.academy.skillshare.dto.article.CreateArticleRequestDto;
import mate.academy.skillshare.exception.EntityNotFoundException;
import mate.academy.skillshare.mapper.ArticleMapper;
import mate.academy.skillshare.model.Article;
import mate.academy.skillshare.model.Content;
import mate.academy.skillshare.model.Image;
import mate.academy.skillshare.model.Subtitle;
import mate.academy.skillshare.repository.article.ArticleRepository;
import mate.academy.skillshare.service.external.ImageService;
import mate.academy.skillshare.service.internal.ArticleService;
import mate.academy.skillshare.service.internal.ContentService;
import mate.academy.skillshare.service.internal.SubtitleService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {
    private final ArticleRepository articleRepository;
    private final ArticleMapper articleMapper;
    private final SubtitleService subtitleService;
    private final ContentService contentService;
    private final ImageService imageService;

    @Override
    @Transactional
    public ArticleDto create(CreateArticleRequestDto requestDto) {
        Article article = articleMapper.toModel(requestDto);
        article.setSubtitles(processSubtitles(requestDto, article));
        article.setContents(processContents(requestDto, article));
        article.setImages(processImages(requestDto, article));
        return articleMapper.toDto(articleRepository.save(article));
    }

    @Override
    public List<ArticleCardDto> getAll(Pageable pageable) {
        return articleMapper.toCardDtoList(articleRepository.findAll(pageable).getContent());
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
        Article article = articleRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Can't find article by id: " + id));
        article.getImages().forEach(image -> imageService.delete(image.getId()));
        articleRepository.deleteById(id);
    }

    private Set<Subtitle> processSubtitles(CreateArticleRequestDto requestDto, Article article) {
        return requestDto.subtitles() != null
                ? requestDto.subtitles()
                        .stream()
                        .map(subtitle -> subtitleService.createForArticle(article, subtitle))
                        .collect(Collectors.toSet())
                : Collections.emptySet();
    }

    private Set<Content> processContents(CreateArticleRequestDto requestDto, Article article) {
        return requestDto.contents() != null
                ? requestDto.contents()
                        .stream()
                        .map(content -> contentService.createForArticle(article, content))
                        .collect(Collectors.toSet())
                : Collections.emptySet();
    }

    private Set<Image> processImages(CreateArticleRequestDto requestDto, Article article) {
        return requestDto.images() != null
                ? requestDto.images()
                        .stream()
                        .map(image -> {
                            try {
                                return imageService.createForArticle(article, image);
                            } catch (IOException e) {
                                throw new RuntimeException("Can't upload image", e);
                            }
                        }).collect(Collectors.toSet())
                : Collections.emptySet();
    }
}
