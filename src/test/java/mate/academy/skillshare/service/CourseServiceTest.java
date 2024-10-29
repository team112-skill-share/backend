package mate.academy.skillshare.service;

import static mate.academy.skillshare.util.ContentTestUtil.createTestContent;
import static mate.academy.skillshare.util.ContentTestUtil.createTestCreateContentRequestDto;
import static mate.academy.skillshare.util.CourseTestUtil.createTestCourse;
import static mate.academy.skillshare.util.CourseTestUtil.createTestCourseCardDto;
import static mate.academy.skillshare.util.CourseTestUtil.createTestCourseDto;
import static mate.academy.skillshare.util.CourseTestUtil.createTestCreateCourseRequestDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import mate.academy.skillshare.dto.content.CreateContentRequestDto;
import mate.academy.skillshare.dto.course.CourseCardDto;
import mate.academy.skillshare.dto.course.CourseDto;
import mate.academy.skillshare.dto.course.CreateCourseRequestDto;
import mate.academy.skillshare.exception.EntityNotFoundException;
import mate.academy.skillshare.mapper.CourseMapper;
import mate.academy.skillshare.model.Course;
import mate.academy.skillshare.repository.category.CategoryRepository;
import mate.academy.skillshare.repository.course.CourseRepository;
import mate.academy.skillshare.service.internal.ContentService;
import mate.academy.skillshare.service.internal.impl.CourseServiceImpl;
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
public class CourseServiceTest {
    @Mock
    private CourseRepository courseRepository;
    @Mock
    private CourseMapper courseMapper;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private ContentService contentService;

    @InjectMocks
    private CourseServiceImpl courseService;

    @Test
    @DisplayName("Verify that correct CourseDto was returned when calling create() method")
    public void create_WithValidRequestDto_ShouldReturnValidCourseDto() {
        //Given
        Course course = createTestCourse();
        CreateCourseRequestDto requestDto = createTestCreateCourseRequestDto(course);
        CreateContentRequestDto contentRequestDto
                = createTestCreateContentRequestDto(createTestContent());
        CourseDto expected = createTestCourseDto(course);

        when(categoryRepository.findById(requestDto.categoryId()))
                .thenReturn(Optional.of(course.getCategory()));
        when(courseMapper.toModel(requestDto)).thenReturn(course);
        when(contentService.createForCourse(course, contentRequestDto))
                .thenReturn(course.getContents().iterator().next());
        when(courseRepository.save(course)).thenReturn(course);
        when(courseMapper.toDto(course)).thenReturn(expected);
        //When
        CourseDto actual = courseService.create(requestDto);
        //Then
        assertEquals(expected, actual);

        verify(categoryRepository, times(1)).findById(requestDto.categoryId());
        verify(courseMapper, times(1)).toModel(requestDto);
        verify(contentService, times(1)).createForCourse(course, contentRequestDto);
        verify(courseRepository, times(1)).save(course);
        verify(courseMapper, times(1)).toDto(course);
        verifyNoMoreInteractions(
                categoryRepository, courseMapper, contentService, courseRepository);
    }

    @Test
    @DisplayName("Verify that exception is thrown when calling create() method with invalid id")
    public void create_WithInvalidId_ShouldThrowException() {
        //Given
        Long categoryId = 2L;
        Course course = createTestCourse();
        course.getCategory().setId(categoryId);
        CreateCourseRequestDto requestDto = createTestCreateCourseRequestDto(course);

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());
        //When
        Exception exception = assertThrows(
                EntityNotFoundException.class,
                () -> courseService.create(requestDto)
        );
        //Then
        String expected = "Can't find category by id: " + categoryId;
        String actual = exception.getMessage();
        assertEquals(expected, actual);

        verify(categoryRepository, times(1)).findById(requestDto.categoryId());
        verifyNoMoreInteractions(categoryRepository);
    }

    @Test
    @DisplayName("Verify that correct Course list was returned when calling getAll() method")
    public void getAll_WithValidCourse_ShouldReturnCorrectCourseDtoList() {
        //Given
        Course course = createTestCourse();
        CourseCardDto courseDto = createTestCourseCardDto(course);
        Pageable pageable = PageRequest.of(0, 20);
        Page<Course> coursePage = new PageImpl<>(List.of(course), pageable, 1);
        List<CourseCardDto> expected = List.of(courseDto);

        when(courseRepository.findAll(pageable)).thenReturn(coursePage);
        when(courseMapper.toCardDtoList(coursePage.getContent())).thenReturn(expected);
        //When
        List<CourseCardDto> actual = courseService.getAll(pageable);
        //Then
        assertEquals(expected, actual);

        verify(courseRepository, times(1)).findAll(pageable);
        verify(courseMapper, times(1)).toCardDtoList(coursePage.getContent());
        verifyNoMoreInteractions(courseRepository, courseMapper);
    }

    @Test
    @DisplayName("Verify that correct CourseDto was returned when calling get() method")
    public void get_WithValidId_ShouldReturnValidCourseDto() {
        //Given
        Course course = createTestCourse();
        CourseDto expected = createTestCourseDto(course);
        Long id = expected.id();

        when(courseRepository.findById(id)).thenReturn(Optional.of(course));
        when(courseMapper.toDto(course)).thenReturn(expected);
        //When
        CourseDto actual = courseService.get(id);
        //Then
        assertEquals(expected, actual);

        verify(courseRepository, times(1)).findById(id);
        verify(courseMapper, times(1)).toDto(course);
        verifyNoMoreInteractions(courseRepository, courseMapper);
    }

    @Test
    @DisplayName("Verify that exception is thrown when calling get() method with invalid id")
    public void get_WithInvalidId_ShouldThrowException() {
        //Given
        Long id = 2L;

        when(courseRepository.findById(id)).thenReturn(Optional.empty());
        //When
        Exception exception = assertThrows(
                EntityNotFoundException.class,
                () -> courseService.get(id)
        );
        //Then
        String expected = "Can't find course by id: " + id;
        String actual = exception.getMessage();
        assertEquals(expected, actual);

        verify(courseRepository, times(1)).findById(id);
        verifyNoMoreInteractions(courseRepository);
    }

    @Test
    @DisplayName("Verify that correct CourseDto was returned when calling update() method")
    public void update_WithValidRequestDto_ShouldReturnValidCourseDto() {
        //Given
        Course course = createTestCourse();
        Course updatedCourse = createTestCourse();
        updatedCourse.setAuthor("upd author");
        CreateCourseRequestDto requestDto = createTestCreateCourseRequestDto(updatedCourse);
        CourseDto expected = createTestCourseDto(updatedCourse);
        Long id = expected.id();

        when(courseRepository.findById(id)).thenReturn(Optional.of(course));
        when(courseMapper.updateFromRequest(requestDto, course)).thenReturn(course);
        when(courseRepository.save(course)).thenReturn(course);
        when(courseMapper.toDto(course)).thenReturn(expected);
        //When
        CourseDto actual = courseService.update(id, requestDto);
        //Then
        assertEquals(expected, actual);

        verify(courseRepository, times(1)).findById(id);
        verify(courseMapper, times(1)).updateFromRequest(requestDto, course);
        verify(courseRepository, times(1)).save(course);
        verify(courseMapper, times(1)).toDto(course);
        verifyNoMoreInteractions(courseRepository, courseMapper);
    }

    @Test
    @DisplayName(
            "Verify that exception is thrown when calling update() method with invalid category id")
    public void update_WithInvalidCategoryId_ShouldThrowException() {
        //Given
        Long categoryId = 2L;
        Course course = createTestCourse();
        Course updatedCourse = createTestCourse();
        updatedCourse.getCategory().setId(categoryId);
        CreateCourseRequestDto requestDto = createTestCreateCourseRequestDto(updatedCourse);
        Long courseId = course.getId();

        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(courseMapper.updateFromRequest(requestDto, course)).thenReturn(course);
        when(categoryRepository.findById(requestDto.categoryId())).thenReturn(Optional.empty());
        //When
        Exception exception = assertThrows(
                EntityNotFoundException.class,
                () -> courseService.update(courseId, requestDto)
        );
        //Then
        String expected = "Can't find category by id: " + categoryId;
        String actual = exception.getMessage();
        assertEquals(expected, actual);

        verify(courseRepository, times(1)).findById(courseId);
        verify(courseMapper, times(1)).updateFromRequest(requestDto, course);
        verify(categoryRepository, times(1)).findById(requestDto.categoryId());
        verifyNoMoreInteractions(courseRepository, courseMapper, categoryRepository);
    }
}
