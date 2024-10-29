package mate.academy.skillshare.service.internal.impl;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import mate.academy.skillshare.dto.course.CourseCardDto;
import mate.academy.skillshare.dto.course.CourseDto;
import mate.academy.skillshare.dto.course.CourseSearchParameters;
import mate.academy.skillshare.dto.course.CreateCourseRequestDto;
import mate.academy.skillshare.exception.EntityNotFoundException;
import mate.academy.skillshare.mapper.CourseMapper;
import mate.academy.skillshare.model.Category;
import mate.academy.skillshare.model.Content;
import mate.academy.skillshare.model.Course;
import mate.academy.skillshare.model.Image;
import mate.academy.skillshare.repository.category.CategoryRepository;
import mate.academy.skillshare.repository.course.CourseRepository;
import mate.academy.skillshare.repository.course.CourseSpecificationBuilder;
import mate.academy.skillshare.service.external.ImageService;
import mate.academy.skillshare.service.internal.ContentService;
import mate.academy.skillshare.service.internal.CourseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;
    private final CategoryRepository categoryRepository;
    private final CourseSpecificationBuilder courseSpecificationBuilder;
    private final ContentService contentService;
    private final ImageService imageService;

    @Override
    @Transactional
    public CourseDto create(CreateCourseRequestDto requestDto) {
        Category category = categoryRepository.findById(requestDto.categoryId()).orElseThrow(() ->
                new EntityNotFoundException("Can't find category by id: "
                        + requestDto.categoryId()));
        Course course = courseMapper.toModel(requestDto);
        course.setCategory(category);
        course.setContents(processContents(requestDto, course));
        course.setImages(processImages(requestDto, course));
        return courseMapper.toDto(courseRepository.save(course));
    }

    @Override
    public List<CourseCardDto> getAll(Pageable pageable) {
        return courseMapper.toCardDtoList(courseRepository.findAll(pageable).getContent());
    }

    @Override
    public List<CourseCardDto> search(CourseSearchParameters searchParameters, Pageable pageable) {
        Specification<Course> courseSpecification = courseSpecificationBuilder
                .build(searchParameters);
        Page<Course> projectsPage = courseRepository.findAll(courseSpecification, pageable);
        return courseMapper.toCardDtoList(projectsPage.getContent());
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
        Course course = courseRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Can't find course by id: " + id));
        course.getImages().forEach(image -> imageService.delete(image.getId()));
        courseRepository.deleteById(id);
    }

    private Set<Content> processContents(CreateCourseRequestDto requestDto, Course course) {
        return requestDto.contents() != null
                ? requestDto.contents()
                        .stream()
                        .map(content -> contentService.createForCourse(course, content))
                        .collect(Collectors.toSet())
                : Collections.emptySet();
    }

    private Set<Image> processImages(CreateCourseRequestDto requestDto, Course course) {
        return requestDto.images() != null
                ? requestDto.images()
                        .stream()
                        .map(image -> {
                            try {
                                return imageService.createForCourse(course, image);
                            } catch (IOException e) {
                                throw new RuntimeException("Can't upload image", e);
                            }
                        }).collect(Collectors.toSet())
                : Collections.emptySet();
    }
}
