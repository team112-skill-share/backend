package mate.academy.skillshare.mapper;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import mate.academy.skillshare.config.MapperConfig;
import mate.academy.skillshare.dto.course.CourseDto;
import mate.academy.skillshare.dto.course.CreateCourseRequestDto;
import mate.academy.skillshare.model.Course;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class, uses = ReviewMapper.class)
public interface CourseMapper {
    @Mapping(target = "categoryId", source = "category.id")
    CourseDto toDto(Course course);

    Course toModel(CreateCourseRequestDto requestDto);

    List<CourseDto> toDtoList(Collection<Course> courses);

    Set<CourseDto> toDtoSet(Collection<Course> courses);

    @Mapping(target = "id", ignore = true)
    Course updateFromRequest(CreateCourseRequestDto requestDto, @MappingTarget Course course);
}
