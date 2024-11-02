package mate.academy.skillshare.util;

import static mate.academy.skillshare.util.CategoryTestUtil.createTestCategory;
import static mate.academy.skillshare.util.ContentTestUtil.createTestContent;
import static mate.academy.skillshare.util.ContentTestUtil.createTestContentDto;
import static mate.academy.skillshare.util.ContentTestUtil.createTestCreateContentRequestDto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import mate.academy.skillshare.dto.course.CourseCardDto;
import mate.academy.skillshare.dto.course.CourseDto;
import mate.academy.skillshare.dto.course.CreateCourseRequestDto;
import mate.academy.skillshare.model.Course;

public class CourseTestUtil {
    public static Course createTestCourse() {
        Course course = new Course();
        course.setId(1L);
        course.setAuthor("author");
        course.setTitle("title");
        course.setCardImage("image");
        course.setDuration("6 months");
        course.setType(Course.CourseType.ONLINE);
        course.setFormat(Course.Format.PERSONAL);
        course.setCertificate(false);
        course.setTrial(false);
        course.setPrice(BigDecimal.valueOf(4.99));
        course.setCategory(createTestCategory());
        course.setContents(Set.of(createTestContent()));
        return course;
    }

    public static CourseCardDto createTestCourseCardDto(Course course) {
        return new CourseCardDto(
                course.getId(),
                course.getAuthor(),
                course.getTitle(),
                course.getCardImage(),
                course.getDuration(),
                course.getType().toString(),
                course.getFormat().toString(),
                course.isCertificate(),
                course.isTrial(),
                course.getPrice(),
                course.getCategory().getId(),
                course.getSource(),
                null,
                null
        );
    }

    public static CourseDto createTestCourseDto(Course course) {
        return new CourseDto(
                course.getId(),
                course.getAuthor(),
                course.getTitle(),
                course.getDuration(),
                course.getType().toString(),
                course.getFormat().toString(),
                course.isCertificate(),
                course.isTrial(),
                course.getPrice(),
                course.getCategory().getId(),
                course.getSource(),
                Set.of(createTestContentDto(createTestContent())),
                null,
                null
        );
    }

    public static CreateCourseRequestDto createTestCreateCourseRequestDto(Course course) {
        return new CreateCourseRequestDto(
                course.getAuthor(),
                course.getTitle(),
                course.getCardImage(),
                course.getDuration(),
                course.getType(),
                course.getFormat(),
                course.isCertificate(),
                course.isTrial(),
                course.getPrice(),
                course.getCategory().getId(),
                course.getSource(),
                List.of(createTestCreateContentRequestDto(createTestContent())),
                null
        );
    }

    public static List<CourseCardDto> fillExpectedCourseCardDtoList() {
        List<CourseCardDto> courseList = new ArrayList<>();
        courseList.add(new CourseCardDto(1L, "author1", "title1", "image1", "1 month",
                "ONLINE", "PERSONAL", false, true, BigDecimal.valueOf(0.99),
                1L, null, null, null));
        courseList.add(new CourseCardDto(2L, "author2", "title2", "image2", "2 months",
                "OFFLINE", "GROUP", true, false, BigDecimal.valueOf(1.99),
                2L, null, null, null));
        return courseList;
    }
}
