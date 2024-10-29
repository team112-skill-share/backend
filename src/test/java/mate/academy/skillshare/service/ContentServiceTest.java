package mate.academy.skillshare.service;

import static mate.academy.skillshare.util.ArticleTestUtil.createTestArticle;
import static mate.academy.skillshare.util.ContentTestUtil.createTestContent;
import static mate.academy.skillshare.util.ContentTestUtil.createTestContentDto;
import static mate.academy.skillshare.util.ContentTestUtil.createTestCreateContentRequestDto;
import static mate.academy.skillshare.util.CourseTestUtil.createTestCourse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.Set;
import mate.academy.skillshare.dto.content.ContentDto;
import mate.academy.skillshare.dto.content.CreateContentRequestDto;
import mate.academy.skillshare.exception.EntityNotFoundException;
import mate.academy.skillshare.mapper.ContentMapper;
import mate.academy.skillshare.model.Article;
import mate.academy.skillshare.model.Content;
import mate.academy.skillshare.model.Course;
import mate.academy.skillshare.repository.content.ContentRepository;
import mate.academy.skillshare.service.internal.impl.ContentServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ContentServiceTest {
    @Mock
    private ContentRepository contentRepository;
    @Mock
    private ContentMapper contentMapper;

    @InjectMocks
    private ContentServiceImpl contentService;

    @Test
    @DisplayName("""
            Verify that correct Content was returned when calling createForCourse() method""")
    public void createForCourse_WithValidRequestDto_ShouldReturnValidContent() {
        //Given
        Content expected = createTestContent();
        Course course = createTestCourse();
        expected.setCourse(course);
        course.setContents(Set.of(expected));
        CreateContentRequestDto requestDto = createTestCreateContentRequestDto(expected);

        when(contentMapper.toModel(requestDto)).thenReturn(expected);
        when(contentRepository.save(expected)).thenReturn(expected);
        //When
        Content actual = contentService.createForCourse(course, requestDto);
        //Then
        assertEquals(expected, actual);

        verify(contentMapper, times(1)).toModel(requestDto);
        verify(contentRepository, times(1)).save(expected);
        verifyNoMoreInteractions(contentMapper, contentRepository);
    }

    @Test
    @DisplayName("""
            Verify that correct Content was returned when calling createForArticle() method""")
    public void createForArticle_WithValidRequestDto_ShouldReturnValidContent() {
        //Given
        Content expected = createTestContent();
        Article article = createTestArticle();
        expected.setArticle(article);
        article.setContents(Set.of(expected));
        CreateContentRequestDto requestDto = createTestCreateContentRequestDto(expected);

        when(contentMapper.toModel(requestDto)).thenReturn(expected);
        when(contentRepository.save(expected)).thenReturn(expected);
        //When
        Content actual = contentService.createForArticle(article, requestDto);
        //Then
        assertEquals(expected, actual);

        verify(contentMapper, times(1)).toModel(requestDto);
        verify(contentRepository, times(1)).save(expected);
        verifyNoMoreInteractions(contentMapper, contentRepository);
    }

    @Test
    @DisplayName("Verify that correct ContentDto was returned when calling update() method")
    public void update_WithValidRequestDto_ShouldReturnValidContentDto() {
        //Given
        Content content = createTestContent();
        Content updatedContent = createTestContent();
        updatedContent.setText("upd text");
        CreateContentRequestDto requestDto = createTestCreateContentRequestDto(updatedContent);
        ContentDto expected = createTestContentDto(updatedContent);
        Long id = expected.id();

        when(contentRepository.findById(id)).thenReturn(Optional.of(content));
        when(contentRepository.save(content)).thenReturn(content);
        when(contentMapper.toDto(content)).thenReturn(expected);
        //When
        ContentDto actual = contentService.update(id, requestDto);
        //Then
        assertEquals(expected, actual);

        verify(contentRepository, times(1)).findById(id);
        verify(contentRepository, times(1)).save(content);
        verify(contentMapper, times(1)).toDto(content);
        verifyNoMoreInteractions(contentRepository, contentMapper);
    }

    @Test
    @DisplayName("Verify that exception is thrown when calling update() method with invalid id")
    public void update_WithInvalidId_ShouldThrowException() {
        //Given
        CreateContentRequestDto requestDto = createTestCreateContentRequestDto(createTestContent());
        Long id = 2L;

        when(contentRepository.findById(id)).thenReturn(Optional.empty());
        //When
        Exception exception = assertThrows(
                EntityNotFoundException.class,
                () -> contentService.update(id, requestDto)
        );
        //Then
        String expected = "Can't find content by id: " + id;
        String actual = exception.getMessage();
        assertEquals(expected, actual);

        verify(contentRepository, times(1)).findById(id);
        verifyNoMoreInteractions(contentRepository);
    }
}
