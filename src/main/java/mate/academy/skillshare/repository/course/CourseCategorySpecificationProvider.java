package mate.academy.skillshare.repository.course;

import mate.academy.skillshare.model.Course;
import mate.academy.skillshare.repository.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class CourseCategorySpecificationProvider implements SpecificationProvider<Course> {
    private static final String CATEGORY = "category";

    @Override
    public String getKey() {
        return CATEGORY;
    }

    @Override
    public Specification<Course> getSpecification(Long categoryId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get(CATEGORY).get("id"), categoryId);
    }
}
