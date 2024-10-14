package mate.academy.skillshare.repository.course;

import mate.academy.skillshare.model.Course;
import mate.academy.skillshare.repository.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class CourseAuthorSpecificationProvider implements SpecificationProvider<Course> {
    private static final String AUTHOR = "author";

    @Override
    public String getKey() {
        return AUTHOR;
    }

    @Override
    public Specification<Course> getSpecification(String author) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get(AUTHOR), author);
    }
}
