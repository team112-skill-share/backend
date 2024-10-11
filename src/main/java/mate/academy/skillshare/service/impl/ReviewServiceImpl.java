package mate.academy.skillshare.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.skillshare.dto.review.CreateReviewRequestDto;
import mate.academy.skillshare.dto.review.ReviewDto;
import mate.academy.skillshare.exception.EntityNotFoundException;
import mate.academy.skillshare.mapper.ReviewMapper;
import mate.academy.skillshare.model.Course;
import mate.academy.skillshare.model.Review;
import mate.academy.skillshare.model.User;
import mate.academy.skillshare.repository.course.CourseRepository;
import mate.academy.skillshare.repository.review.ReviewRepository;
import mate.academy.skillshare.repository.user.UserRepository;
import mate.academy.skillshare.service.ReviewService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    @Override
    public ReviewDto create(Long userId, Long courseId, CreateReviewRequestDto requestDto) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new EntityNotFoundException("Can't find user by id: " + userId));
        Course course = courseRepository.findById(courseId).orElseThrow(() ->
                new EntityNotFoundException("Can't find course by id: " + courseId));
        Review review = reviewMapper.toModel(requestDto);
        review.setUser(user);
        review.setCourse(course);
        return reviewMapper.toDto(reviewRepository.save(review));
    }

    @Override
    public List<ReviewDto> getAll(Long courseId, Pageable pageable) {
        return reviewMapper.toDtoList(
                reviewRepository.findAllByCourseId(courseId, pageable).getContent());
    }

    @Override
    public void delete(Long id) {
        reviewRepository.deleteById(id);
    }
}
