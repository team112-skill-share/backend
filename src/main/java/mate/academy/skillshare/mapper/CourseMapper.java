package mate.academy.skillshare.mapper;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import mate.academy.skillshare.config.MapperConfig;
import mate.academy.skillshare.dto.course.CourseCardDto;
import mate.academy.skillshare.dto.course.CourseDto;
import mate.academy.skillshare.dto.course.CreateCourseRequestDto;
import mate.academy.skillshare.model.Course;
import mate.academy.skillshare.model.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class,
        uses = {ReviewMapper.class, ContentMapper.class, ImageMapper.class})
public interface CourseMapper {
    @Mapping(target = "categoryId", source = "category.id")
    CourseDto toDto(Course course);

    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "averageRating",
            expression = "java(calculateAverageRating(course.getReviews()))")
    @Mapping(target = "reviewsCount",
            expression = "java(course.getReviews() == null "
                    + "|| course.getReviews().isEmpty() ? null : course.getReviews().size())")
    CourseCardDto toCardDto(Course course);

    @Mapping(target = "images", ignore = true)
    Course toModel(CreateCourseRequestDto requestDto);

    List<CourseCardDto> toCardDtoList(Collection<Course> courses);

    Set<CourseCardDto> toCardDtoSet(Collection<Course> courses);

    @Mapping(target = "id", ignore = true)
    Course updateFromRequest(CreateCourseRequestDto requestDto, @MappingTarget Course course);

    default Double calculateAverageRating(List<Review> reviews) {
        if (reviews == null || reviews.isEmpty()) {
            return null;
        }
        return reviews.stream()
                .mapToInt(Review::getRating)
                .average()
                .getAsDouble();
    }
}
