package mate.academy.skillshare.service;

import static mate.academy.skillshare.util.ArticleTestUtil.createTestArticle;
import static mate.academy.skillshare.util.ArticleTestUtil.createTestArticleCardDto;
import static mate.academy.skillshare.util.ArticleTestUtil.createTestArticleDto;
import static mate.academy.skillshare.util.ArticleTestUtil.createTestCreateArticleRequestDto;
import static mate.academy.skillshare.util.SubtitleTestUtil.createTestCreateSubtitleRequestDto;
import static mate.academy.skillshare.util.SubtitleTestUtil.createTestSubtitle;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import mate.academy.skillshare.dto.article.ArticleCardDto;
import mate.academy.skillshare.dto.article.ArticleDto;
import mate.academy.skillshare.dto.article.CreateArticleRequestDto;
import mate.academy.skillshare.dto.subtitle.CreateSubtitleRequestDto;
import mate.academy.skillshare.exception.EntityNotFoundException;
import mate.academy.skillshare.mapper.ArticleMapper;
import mate.academy.skillshare.model.Article;
import mate.academy.skillshare.repository.article.ArticleRepository;
import mate.academy.skillshare.service.internal.SubtitleService;
import mate.academy.skillshare.service.internal.impl.ArticleServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class ArticleServiceTest {
    @Mock
    private ArticleRepository articleRepository;
    @Mock
    private ArticleMapper articleMapper;
    @Mock
    private SubtitleService subtitleService;

    @InjectMocks
    private ArticleServiceImpl articleService;

    @Test
    @DisplayName("Verify that correct ArticleDto was returned when calling create() method")
    public void create_WithValidRequestDto_ShouldReturnValidArticleDto() {
        //Given
        Article article = createTestArticle();
        CreateArticleRequestDto requestDto = createTestCreateArticleRequestDto(article);
        CreateSubtitleRequestDto subtitleRequestDto
                = createTestCreateSubtitleRequestDto(createTestSubtitle());
        ArticleDto expected = createTestArticleDto(article);

        when(articleMapper.toModel(requestDto)).thenReturn(article);
        when(subtitleService.createForArticle(article, subtitleRequestDto))
                .thenReturn(article.getSubtitles().iterator().next());
        when(articleRepository.save(article)).thenReturn(article);
        when(articleMapper.toDto(article)).thenReturn(expected);
        //When
        ArticleDto actual = articleService.create(requestDto);
        //Then
        assertEquals(expected, actual);

        verify(articleMapper, times(1)).toModel(requestDto);
        verify(subtitleService, times(1)).createForArticle(article, subtitleRequestDto);
        verify(articleRepository, times(1)).save(article);
        verify(articleMapper, times(1)).toDto(article);
        verifyNoMoreInteractions(
                articleMapper, subtitleService, articleRepository);
    }

    @Test
    @DisplayName("Verify that correct Article list was returned when calling getAll() method")
    public void getAll_WithValidArticle_ShouldReturnCorrectArticleDtoList() {
        //Given
        Article article = createTestArticle();
        ArticleCardDto articleDto = createTestArticleCardDto(article);
        Pageable pageable = PageRequest.of(0, 20);
        Page<Article> articlePage = new PageImpl<>(List.of(article), pageable, 1);
        List<ArticleCardDto> expected = List.of(articleDto);

        when(articleRepository.findAll(pageable)).thenReturn(articlePage);
        when(articleMapper.toCardDtoList(articlePage.getContent())).thenReturn(expected);
        //When
        List<ArticleCardDto> actual = articleService.getAll(pageable);
        //Then
        assertEquals(expected, actual);

        verify(articleRepository, times(1)).findAll(pageable);
        verify(articleMapper, times(1)).toCardDtoList(articlePage.getContent());
        verifyNoMoreInteractions(articleRepository, articleMapper);
    }

    @Test
    @DisplayName("Verify that correct ArticleDto was returned when calling get() method")
    public void get_WithValidId_ShouldReturnValidArticleDto() {
        //Given
        Article article = createTestArticle();
        ArticleDto expected = createTestArticleDto(article);
        Long id = expected.id();

        when(articleRepository.findById(id)).thenReturn(Optional.of(article));
        when(articleMapper.toDto(article)).thenReturn(expected);
        //When
        ArticleDto actual = articleService.get(id);
        //Then
        assertEquals(expected, actual);

        verify(articleRepository, times(1)).findById(id);
        verify(articleMapper, times(1)).toDto(article);
        verifyNoMoreInteractions(articleRepository, articleMapper);
    }

    @Test
    @DisplayName("Verify that exception is thrown when calling get() method with invalid id")
    public void get_WithInvalidId_ShouldThrowException() {
        //Given
        Long id = 2L;

        when(articleRepository.findById(id)).thenReturn(Optional.empty());
        //When
        Exception exception = assertThrows(
                EntityNotFoundException.class,
                () -> articleService.get(id)
        );
        //Then
        String expected = "Can't find article by id: " + id;
        String actual = exception.getMessage();
        assertEquals(expected, actual);

        verify(articleRepository, times(1)).findById(id);
        verifyNoMoreInteractions(articleRepository);
    }

    @Test
    @DisplayName("Verify that correct ArticleDto was returned when calling update() method")
    public void update_WithValidRequestDto_ShouldReturnValidArticleDto() {
        //Given
        Article article = createTestArticle();
        Article updatedArticle = createTestArticle();
        updatedArticle.setAuthor("upd author");
        CreateArticleRequestDto requestDto = createTestCreateArticleRequestDto(updatedArticle);
        ArticleDto expected = createTestArticleDto(updatedArticle);
        Long id = expected.id();

        when(articleRepository.findById(id)).thenReturn(Optional.of(article));
        when(articleMapper.updateFromRequest(requestDto, article)).thenReturn(article);
        when(articleRepository.save(article)).thenReturn(article);
        when(articleMapper.toDto(article)).thenReturn(expected);
        //When
        ArticleDto actual = articleService.update(id, requestDto);
        //Then
        assertEquals(expected, actual);

        verify(articleRepository, times(1)).findById(id);
        verify(articleMapper, times(1)).updateFromRequest(requestDto, article);
        verify(articleRepository, times(1)).save(article);
        verify(articleMapper, times(1)).toDto(article);
        verifyNoMoreInteractions(articleRepository, articleMapper);
    }
}
