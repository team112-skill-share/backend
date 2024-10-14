package mate.academy.skillshare.repository.course;

import mate.academy.skillshare.model.Course;
import mate.academy.skillshare.repository.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class CoursePriceSpecificationProvider implements SpecificationProvider<Course> {
    private static final String PRICE = "price";

    @Override
    public String getKey() {
        return PRICE;
    }

    @Override
    public Specification<Course> getOrderByPriceAsc() {
        return (root, query, criteriaBuilder) -> {
            query.orderBy(criteriaBuilder.asc(root.get(PRICE)));
            return criteriaBuilder.conjunction();
        };
    }

    @Override
    public Specification<Course> getOrderByPriceDesc() {
        return (root, query, criteriaBuilder) -> {
            query.orderBy(criteriaBuilder.desc(root.get(PRICE)));
            return criteriaBuilder.conjunction();
        };
    }
}
