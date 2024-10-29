package mate.academy.skillshare.dto.course;

public record CourseSearchParameters(
        Long categoryId,
        String author,
        String orderBy
) {
}
