package mate.academy.skillshare.service.internal;

import java.util.List;
import mate.academy.skillshare.dto.course.CourseCardDto;
import mate.academy.skillshare.dto.course.CourseDto;
import mate.academy.skillshare.dto.course.CourseSearchParameters;
import mate.academy.skillshare.dto.course.CreateCourseRequestDto;
import org.springframework.data.domain.Pageable;

public interface CourseService {
    CourseDto create(CreateCourseRequestDto requestDto);

    List<CourseCardDto> getAll(Pageable pageable);

    List<CourseCardDto> search(CourseSearchParameters searchParameters, Pageable pageable);

    CourseDto get(Long id);

    CourseDto update(Long id, CreateCourseRequestDto requestDto);

    void delete(Long id);
}
