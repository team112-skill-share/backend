package mate.academy.skillshare.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.skillshare.dto.review.CreateReviewRequestDto;
import mate.academy.skillshare.dto.review.ReviewDto;
import mate.academy.skillshare.model.User;
import mate.academy.skillshare.service.ReviewService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Review managing", description = "Endpoints for managing reviews")
@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    @Operation(summary = "Create a review", description = "Create a new review")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/course/{courseId}")
    public ReviewDto createReview(
            Authentication authentication,
            @PathVariable Long courseId,
            @RequestBody @Valid CreateReviewRequestDto requestDto
    ) {
        User user = (User) authentication.getPrincipal();
        return reviewService.create(user.getId(), courseId, requestDto);
    }

    @Operation(summary = "Retrieve reviews", description = "Retrieve all reviews for a course")
    @GetMapping("/course/{courseId}")
    public List<ReviewDto> getAllReviews(
            @PathVariable Long courseId,
            @ParameterObject @PageableDefault Pageable pageable) {
        return reviewService.getAll(courseId, pageable);
    }

    @Operation(summary = "Update a review", description = "Update a review by id")
    @PatchMapping("/{id}")
    public ReviewDto updateReview(Authentication authentication,
                             @PathVariable Long id,
                             @RequestBody @Valid CreateReviewRequestDto requestDto) {
        User user = (User) authentication.getPrincipal();
        return reviewService.update(user.getId(), id, requestDto);
    }

    @Operation(summary = "Delete a review", description = "Delete a review by id")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteReview(@PathVariable Long id) {
        reviewService.delete(id);
    }
}
