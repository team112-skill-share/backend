package mate.academy.skillshare.dto.course;

import mate.academy.skillshare.model.Category;

public record CourseSearchParameters(
        Category category,
        String author,
        String orderBy
) {
}
