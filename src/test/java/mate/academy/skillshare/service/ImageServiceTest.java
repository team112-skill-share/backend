package mate.academy.skillshare.service;

import static mate.academy.skillshare.util.ArticleTestUtil.createTestArticle;
import static mate.academy.skillshare.util.CourseTestUtil.createTestCourse;
import static mate.academy.skillshare.util.ImageTestUtil.createTestCreateImageRequestDto;
import static mate.academy.skillshare.util.ImageTestUtil.createTestImage;
import static mate.academy.skillshare.util.ImageTestUtil.createTestImageDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.Set;
import mate.academy.skillshare.dto.image.CreateImageRequestDto;
import mate.academy.skillshare.dto.image.ImageDto;
import mate.academy.skillshare.exception.EntityNotFoundException;
import mate.academy.skillshare.mapper.ImageMapper;
import mate.academy.skillshare.model.Article;
import mate.academy.skillshare.model.Course;
import mate.academy.skillshare.model.Image;
import mate.academy.skillshare.repository.image.ImageRepository;
import mate.academy.skillshare.service.internal.impl.ImageServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ImageServiceTest {
    @Mock
    private ImageRepository imageRepository;
    @Mock
    private ImageMapper imageMapper;

    @InjectMocks
    private ImageServiceImpl imageService;

    @Test
    @DisplayName("""
            Verify that correct Image was returned when calling createForCourse() method""")
    public void createForCourse_WithValidRequestDto_ShouldReturnValidImage() {
        //Given
        Image expected = createTestImage();
        Course course = createTestCourse();
        expected.setCourse(course);
        course.setImages(Set.of(expected));
        CreateImageRequestDto requestDto = createTestCreateImageRequestDto(expected);

        when(imageMapper.toModel(requestDto)).thenReturn(expected);
        when(imageRepository.save(expected)).thenReturn(expected);
        //When
        Image actual = imageService.createForCourse(course, requestDto);
        //Then
        assertEquals(expected, actual);

        verify(imageMapper, times(1)).toModel(requestDto);
        verify(imageRepository, times(1)).save(expected);
        verifyNoMoreInteractions(imageMapper, imageRepository);
    }

    @Test
    @DisplayName("""
            Verify that correct Image was returned when calling createForArticle() method""")
    public void createForArticle_WithValidRequestDto_ShouldReturnValidImage() {
        //Given
        Image expected = createTestImage();
        Article article = createTestArticle();
        expected.setArticle(article);
        article.setImages(Set.of(expected));
        CreateImageRequestDto requestDto = createTestCreateImageRequestDto(expected);

        when(imageMapper.toModel(requestDto)).thenReturn(expected);
        when(imageRepository.save(expected)).thenReturn(expected);
        //When
        Image actual = imageService.createForArticle(article, requestDto);
        //Then
        assertEquals(expected, actual);

        verify(imageMapper, times(1)).toModel(requestDto);
        verify(imageRepository, times(1)).save(expected);
        verifyNoMoreInteractions(imageMapper, imageRepository);
    }

    @Test
    @DisplayName("Verify that correct ImageDto was returned when calling update() method")
    public void update_WithValidRequestDto_ShouldReturnValidImageDto() {
        //Given
        Image image = createTestImage();
        Image updatedImage = createTestImage();
        updatedImage.setUrl("upd url");
        CreateImageRequestDto requestDto = createTestCreateImageRequestDto(updatedImage);
        ImageDto expected = createTestImageDto(updatedImage);
        Long id = expected.id();

        when(imageRepository.findById(id)).thenReturn(Optional.of(image));
        when(imageRepository.save(image)).thenReturn(image);
        when(imageMapper.toDto(image)).thenReturn(expected);
        //When
        ImageDto actual = imageService.update(id, requestDto);
        //Then
        assertEquals(expected, actual);

        verify(imageRepository, times(1)).findById(id);
        verify(imageRepository, times(1)).save(image);
        verify(imageMapper, times(1)).toDto(image);
        verifyNoMoreInteractions(imageRepository, imageMapper);
    }

    @Test
    @DisplayName("Verify that exception is thrown when calling update() method with invalid id")
    public void update_WithInvalidId_ShouldThrowException() {
        //Given
        CreateImageRequestDto requestDto = createTestCreateImageRequestDto(createTestImage());
        Long id = 2L;

        when(imageRepository.findById(id)).thenReturn(Optional.empty());
        //When
        Exception exception = assertThrows(
                EntityNotFoundException.class,
                () -> imageService.update(id, requestDto)
        );
        //Then
        String expected = "Can't find image by id: " + id;
        String actual = exception.getMessage();
        assertEquals(expected, actual);

        verify(imageRepository, times(1)).findById(id);
        verifyNoMoreInteractions(imageRepository);
    }
}
