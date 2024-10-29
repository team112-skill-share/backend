package mate.academy.skillshare.service;

import static mate.academy.skillshare.util.ArticleTestUtil.createTestArticle;
import static mate.academy.skillshare.util.SubtitleTestUtil.createTestCreateSubtitleRequestDto;
import static mate.academy.skillshare.util.SubtitleTestUtil.createTestSubtitle;
import static mate.academy.skillshare.util.SubtitleTestUtil.createTestSubtitleDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.Set;
import mate.academy.skillshare.dto.subtitle.CreateSubtitleRequestDto;
import mate.academy.skillshare.dto.subtitle.SubtitleDto;
import mate.academy.skillshare.exception.EntityNotFoundException;
import mate.academy.skillshare.mapper.SubtitleMapper;
import mate.academy.skillshare.model.Article;
import mate.academy.skillshare.model.Subtitle;
import mate.academy.skillshare.repository.subtitle.SubtitleRepository;
import mate.academy.skillshare.service.internal.impl.SubtitleServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class SubtitleServiceTest {
    @Mock
    private SubtitleRepository subtitleRepository;
    @Mock
    private SubtitleMapper subtitleMapper;

    @InjectMocks
    private SubtitleServiceImpl subtitleService;

    @Test
    @DisplayName("""
            Verify that correct Subtitle was returned when calling createForArticle() method""")
    public void createForArticle_WithValidRequestDto_ShouldReturnValidContent() {
        //Given
        Subtitle expected = createTestSubtitle();
        Article article = createTestArticle();
        expected.setArticle(article);
        article.setSubtitles(Set.of(expected));
        CreateSubtitleRequestDto requestDto = createTestCreateSubtitleRequestDto(expected);

        when(subtitleMapper.toModel(requestDto)).thenReturn(expected);
        when(subtitleRepository.save(expected)).thenReturn(expected);
        //When
        Subtitle actual = subtitleService.createForArticle(article, requestDto);
        //Then
        assertEquals(expected, actual);

        verify(subtitleMapper, times(1)).toModel(requestDto);
        verify(subtitleRepository, times(1)).save(expected);
        verifyNoMoreInteractions(subtitleMapper, subtitleRepository);
    }

    @Test
    @DisplayName("Verify that correct SubtitleDto was returned when calling update() method")
    public void update_WithValidRequestDto_ShouldReturnValidSubtitleDto() {
        //Given
        Subtitle subtitle = createTestSubtitle();
        Subtitle updatedSubtitle = createTestSubtitle();
        updatedSubtitle.setSubtitle("upd subtitle");
        CreateSubtitleRequestDto requestDto = createTestCreateSubtitleRequestDto(updatedSubtitle);
        SubtitleDto expected = createTestSubtitleDto(updatedSubtitle);
        Long id = expected.id();

        when(subtitleRepository.findById(id)).thenReturn(Optional.of(subtitle));
        when(subtitleRepository.save(subtitle)).thenReturn(subtitle);
        when(subtitleMapper.toDto(subtitle)).thenReturn(expected);
        //When
        SubtitleDto actual = subtitleService.update(id, requestDto);
        //Then
        assertEquals(expected, actual);

        verify(subtitleRepository, times(1)).findById(id);
        verify(subtitleRepository, times(1)).save(subtitle);
        verify(subtitleMapper, times(1)).toDto(subtitle);
        verifyNoMoreInteractions(subtitleRepository, subtitleMapper);
    }

    @Test
    @DisplayName("Verify that exception is thrown when calling update() method with invalid id")
    public void update_WithInvalidId_ShouldThrowException() {
        //Given
        CreateSubtitleRequestDto requestDto
                = createTestCreateSubtitleRequestDto(createTestSubtitle());
        Long id = 2L;

        when(subtitleRepository.findById(id)).thenReturn(Optional.empty());
        //When
        Exception exception = assertThrows(
                EntityNotFoundException.class,
                () -> subtitleService.update(id, requestDto)
        );
        //Then
        String expected = "Can't find subtitle by id: " + id;
        String actual = exception.getMessage();
        assertEquals(expected, actual);

        verify(subtitleRepository, times(1)).findById(id);
        verifyNoMoreInteractions(subtitleRepository);
    }
}
