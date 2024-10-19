package mate.academy.skillshare.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.skillshare.dto.course.CourseDto;
import mate.academy.skillshare.dto.course.CourseSearchParameters;
import mate.academy.skillshare.dto.course.CreateCourseRequestDto;
import mate.academy.skillshare.service.internal.CourseService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Course managing", description = "Endpoints for managing courses")
@RestController
@RequiredArgsConstructor
@RequestMapping("/courses")
public class CourseController {
    private final CourseService courseService;

    @Operation(summary = "Create a course", description = "Create a new course")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public CourseDto createCourse(@RequestBody @Valid CreateCourseRequestDto requestDto) {
        return courseService.create(requestDto);
    }

    @Operation(summary = "Retrieve courses", description = "Retrieve all courses")
    @GetMapping
    public List<CourseDto> getAllCourses(@ParameterObject @PageableDefault Pageable pageable) {
        return courseService.getAll(pageable);
    }

    @Operation(summary = "Search for specific courses",
            description = "Search for specific courses by required criteria")
    @GetMapping("/search")
    public List<CourseDto> searchCourses(CourseSearchParameters searchParameters,
                                         @ParameterObject @PageableDefault Pageable pageable) {
        return courseService.search(searchParameters, pageable);
    }

    @Operation(summary = "Retrieve a course", description = "Retrieve a course by id")
    @GetMapping("/{id}")
    public CourseDto getCourse(@PathVariable Long id) {
        return courseService.get(id);
    }

    @Operation(summary = "Update a course", description = "Update a course by id")
    @PutMapping("/{id}")
    public CourseDto updateCourse(@PathVariable Long id,
                                  @RequestBody CreateCourseRequestDto requestDto) {
        return courseService.update(id, requestDto);
    }

    @Operation(summary = "Delete a course", description = "Delete a course by id")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteCourse(@PathVariable Long id) {
        courseService.delete(id);
    }
}
