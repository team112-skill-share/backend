package mate.academy.skillshare.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.skillshare.dto.course.CourseDto;
import mate.academy.skillshare.dto.course.CourseSearchParameters;
import mate.academy.skillshare.dto.course.CreateCourseRequestDto;
import mate.academy.skillshare.exception.EntityNotFoundException;
import mate.academy.skillshare.mapper.CourseMapper;
import mate.academy.skillshare.model.Category;
import mate.academy.skillshare.model.Course;
import mate.academy.skillshare.repository.category.CategoryRepository;
import mate.academy.skillshare.repository.course.CourseRepository;
import mate.academy.skillshare.repository.course.CourseSpecificationBuilder;
import mate.academy.skillshare.service.CourseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;
    private final CategoryRepository categoryRepository;
    private final CourseSpecificationBuilder courseSpecificationBuilder;

    @Override
    public CourseDto create(CreateCourseRequestDto requestDto) {
        Category category = categoryRepository.findById(requestDto.categoryId()).orElseThrow(() ->
                new EntityNotFoundException("Can't find category by id: "
                        + requestDto.categoryId()));
        Course course = courseMapper.toModel(requestDto);
        course.setCategory(category);
        return courseMapper.toDto(courseRepository.save(course));
    }

    @Override
    public List<CourseDto> getAll(Pageable pageable) {
        return courseMapper.toDtoList(courseRepository.findAll(pageable).getContent());
    }

    @Override
    public List<CourseDto> search(CourseSearchParameters searchParameters, Pageable pageable) {
        Specification<Course> courseSpecification = courseSpecificationBuilder
                .build(searchParameters);
        Page<Course> projectsPage = courseRepository.findAll(courseSpecification, pageable);
        return courseMapper.toDtoList(projectsPage.getContent());
    }

    @Override
    public CourseDto get(Long id) {
        return courseMapper.toDto(courseRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Can't find course by id: " + id)));
    }

    @Override
    public CourseDto update(Long id, CreateCourseRequestDto requestDto) {
        Course existingCourse = courseRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Can't find course by id: " + id));
        Course updatedCourse = courseMapper.updateFromRequest(requestDto, existingCourse);
        Category category = existingCourse.getCategory();
        if (!category.getId().equals(requestDto.categoryId())) {
            category = categoryRepository.findById(requestDto.categoryId())
                    .orElseThrow(() -> new EntityNotFoundException("Can't find category by id: "
                            + requestDto.categoryId()));
        }
        updatedCourse.setCategory(category);
        return courseMapper.toDto(courseRepository.save(updatedCourse));
    }

    @Override
    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }
}
