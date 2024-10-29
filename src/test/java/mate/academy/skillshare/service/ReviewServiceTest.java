package mate.academy.skillshare.service;

import static mate.academy.skillshare.util.ReviewTestUtil.createTestCreateReviewRequestDto;
import static mate.academy.skillshare.util.ReviewTestUtil.createTestReview;
import static mate.academy.skillshare.util.ReviewTestUtil.createTestReviewDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import mate.academy.skillshare.dto.review.CreateReviewRequestDto;
import mate.academy.skillshare.dto.review.ReviewDto;
import mate.academy.skillshare.exception.AuthenticationException;
import mate.academy.skillshare.exception.EntityNotFoundException;
import mate.academy.skillshare.mapper.ReviewMapper;
import mate.academy.skillshare.model.Review;
import mate.academy.skillshare.repository.course.CourseRepository;
import mate.academy.skillshare.repository.review.ReviewRepository;
import mate.academy.skillshare.repository.user.UserRepository;
import mate.academy.skillshare.service.internal.impl.ReviewServiceImpl;
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
public class ReviewServiceTest {
    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private ReviewMapper reviewMapper;
    @Mock
    private CourseRepository courseRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ReviewServiceImpl reviewService;

    @Test
    @DisplayName("Verify that correct ReviewDto was returned when calling create() method")
    public void create_WithValidRequestDto_ShouldReturnValidReviewDto() {
        //Given
        Review review = createTestReview();
        CreateReviewRequestDto requestDto = createTestCreateReviewRequestDto(review);
        ReviewDto expected = createTestReviewDto(review);
        Long userId = review.getUser().getId();
        Long courseId = review.getCourse().getId();

        when(userRepository.findById(userId)).thenReturn(Optional.of(review.getUser()));
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(review.getCourse()));
        when(reviewMapper.toModel(requestDto)).thenReturn(review);
        when(reviewRepository.save(review)).thenReturn(review);
        when(reviewMapper.toDto(review)).thenReturn(expected);
        //When
        ReviewDto actual = reviewService.create(userId, courseId, requestDto);
        //Then
        assertEquals(expected, actual);

        verify(userRepository, times(1)).findById(userId);
        verify(courseRepository, times(1)).findById(courseId);
        verify(reviewMapper, times(1)).toModel(requestDto);
        verify(reviewRepository, times(1)).save(review);
        verify(reviewMapper, times(1)).toDto(review);
        verifyNoMoreInteractions(userRepository, courseRepository, reviewMapper, reviewRepository);
    }

    @Test
    @DisplayName(
            "Verify that exception is thrown when calling create() method with invalid user id")
    public void create_WithInvalidUserId_ShouldThrowException() {
        //Given
        Review review = createTestReview();
        CreateReviewRequestDto requestDto = createTestCreateReviewRequestDto(review);
        Long userId = 2L;
        Long courseId = review.getCourse().getId();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        //When
        Exception exception = assertThrows(
                EntityNotFoundException.class,
                () -> reviewService.create(userId, courseId, requestDto)
        );
        //Then
        String expected = "Can't find user by id: " + userId;
        String actual = exception.getMessage();
        assertEquals(expected, actual);

        verify(userRepository, times(1)).findById(userId);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    @DisplayName(
            "Verify that exception is thrown when calling create() method with invalid course id")
    public void create_WithInvalidCourseId_ShouldThrowException() {
        //Given
        Review review = createTestReview();
        CreateReviewRequestDto requestDto = createTestCreateReviewRequestDto(review);
        Long userId = review.getUser().getId();
        Long courseId = 2L;

        when(userRepository.findById(userId)).thenReturn(Optional.of(review.getUser()));
        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());
        //When
        Exception exception = assertThrows(
                EntityNotFoundException.class,
                () -> reviewService.create(userId, courseId, requestDto)
        );
        //Then
        String expected = "Can't find course by id: " + courseId;
        String actual = exception.getMessage();
        assertEquals(expected, actual);

        verify(userRepository, times(1)).findById(userId);
        verify(courseRepository, times(1)).findById(courseId);
        verifyNoMoreInteractions(userRepository, courseRepository);
    }

    @Test
    @DisplayName("Verify that correct Review list was returned when calling getAll() method")
    public void getAll_WithValidReview_ShouldReturnCorrectReviewDtoList() {
        //Given
        Review review = createTestReview();
        ReviewDto reviewDto = createTestReviewDto(review);
        Pageable pageable = PageRequest.of(0, 20);
        Page<Review> coursePage = new PageImpl<>(List.of(review), pageable, 1);
        List<ReviewDto> expected = List.of(reviewDto);
        Long courseId = review.getCourse().getId();

        when(reviewRepository.findAllByCourseId(courseId, pageable)).thenReturn(coursePage);
        when(reviewMapper.toDtoList(coursePage.getContent())).thenReturn(expected);
        //When
        List<ReviewDto> actual = reviewService.getAll(courseId, pageable);
        //Then
        assertEquals(expected, actual);

        verify(reviewRepository, times(1)).findAllByCourseId(courseId, pageable);
        verify(reviewMapper, times(1)).toDtoList(coursePage.getContent());
        verifyNoMoreInteractions(reviewRepository, reviewMapper);
    }

    @Test
    @DisplayName("Verify that correct ReviewDto was returned when calling update() method")
    public void update_WithValidRequestDto_ShouldReturnValidReviewDto() {
        //Given
        Review review = createTestReview();
        Review updatedReview = createTestReview();
        updatedReview.setRating(2);
        CreateReviewRequestDto requestDto = createTestCreateReviewRequestDto(updatedReview);
        ReviewDto expected = createTestReviewDto(updatedReview);
        Long id = expected.id();

        when(reviewRepository.findById(id)).thenReturn(Optional.of(review));
        when(reviewRepository.save(review)).thenReturn(review);
        when(reviewMapper.toDto(review)).thenReturn(expected);
        //When
        ReviewDto actual = reviewService.update(review.getUser().getId(), id, requestDto);
        //Then
        assertEquals(expected, actual);

        verify(reviewRepository, times(1)).findById(id);
        verify(reviewRepository, times(1)).save(review);
        verify(reviewMapper, times(1)).toDto(review);
        verifyNoMoreInteractions(reviewRepository, reviewMapper);
    }

    @Test
    @DisplayName(
            "Verify that exception is thrown when calling update() method with invalid id")
    public void update_WithInvalidId_ShouldThrowException() {
        //Given
        Review review = createTestReview();
        CreateReviewRequestDto requestDto = createTestCreateReviewRequestDto(review);
        Long userId = review.getUser().getId();
        Long id = 2L;

        when(reviewRepository.findById(id)).thenReturn(Optional.empty());
        //When
        Exception exception = assertThrows(
                EntityNotFoundException.class,
                () -> reviewService.update(userId, id, requestDto)
        );
        //Then
        String expected = "Can't find review by id: " + id;
        String actual = exception.getMessage();
        assertEquals(expected, actual);

        verify(reviewRepository, times(1)).findById(id);
        verifyNoMoreInteractions(reviewRepository);
    }

    @Test
    @DisplayName(
            "Verify that exception is thrown when calling update() method with invalid user id")
    public void update_WithInvalidUserId_ShouldThrowException() {
        //Given
        Review review = createTestReview();
        CreateReviewRequestDto requestDto = createTestCreateReviewRequestDto(review);
        Long userId = 2L;
        Long id = review.getId();

        when(reviewRepository.findById(id)).thenReturn(Optional.of(review));
        //When
        Exception exception = assertThrows(
                AuthenticationException.class,
                () -> reviewService.update(userId, id, requestDto)
        );
        //Then
        String expected = "Review can be updated only by its creator";
        String actual = exception.getMessage();
        assertEquals(expected, actual);

        verify(reviewRepository, times(1)).findById(id);
        verifyNoMoreInteractions(reviewRepository);
    }
}
