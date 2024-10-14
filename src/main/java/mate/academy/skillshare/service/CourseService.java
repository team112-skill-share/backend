package mate.academy.skillshare.service;

import java.util.List;
import mate.academy.skillshare.dto.course.CourseDto;
import mate.academy.skillshare.dto.course.CourseSearchParameters;
import mate.academy.skillshare.dto.course.CreateCourseRequestDto;
import org.springframework.data.domain.Pageable;

public interface CourseService {
    CourseDto create(CreateCourseRequestDto requestDto);

    List<CourseDto> getAll(Pageable pageable);

    List<CourseDto> search(CourseSearchParameters searchParameters, Pageable pageable);

    CourseDto get(Long id);

    CourseDto update(Long id, CreateCourseRequestDto requestDto);

    void delete(Long id);
}
