package mate.academy.skillshare.repository.course;

import mate.academy.skillshare.model.Course;
import mate.academy.skillshare.repository.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class CourseRatingSpecificationProvider implements SpecificationProvider<Course> {
    private static final String RATING = "rating";

    @Override
    public String getKey() {
        return RATING;
    }

    @Override
    public Specification<Course> getOrderByRatingDesc() {
        return (root, query, criteriaBuilder) -> {
            query.orderBy(criteriaBuilder.desc(criteriaBuilder
                    .avg(root.join("reviews").get(RATING))));
            return criteriaBuilder.conjunction();
        };
    }
}
