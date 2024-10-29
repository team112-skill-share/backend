package mate.academy.skillshare.repository.course;

import lombok.RequiredArgsConstructor;
import mate.academy.skillshare.dto.course.CourseSearchParameters;
import mate.academy.skillshare.model.Course;
import mate.academy.skillshare.repository.SpecificationBuilder;
import mate.academy.skillshare.repository.SpecificationProviderManager;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CourseSpecificationBuilder
        implements SpecificationBuilder<Course, CourseSearchParameters> {
    private final SpecificationProviderManager<Course> courseSpecificationProviderManager;

    @Override
    public Specification<Course> build(CourseSearchParameters searchParameters) {
        Specification<Course> spec = Specification.where(null);
        if (searchParameters.categoryId() != null) {
            spec = spec.and(courseSpecificationProviderManager.getSpecificationProvider("category")
                    .getSpecification(searchParameters.categoryId()));
        }
        if (searchParameters.author() != null && !searchParameters.author().isEmpty()) {
            spec = spec.and(courseSpecificationProviderManager.getSpecificationProvider("author")
                    .getSpecification(searchParameters.author()));
        }
        if (searchParameters.orderBy() != null) {
            switch (searchParameters.orderBy()) {
                case "priceAsc":
                    spec = spec.and(courseSpecificationProviderManager
                            .getSpecificationProvider("price").getOrderByPriceAsc());
                    break;
                case "priceDesc":
                    spec = spec.and(courseSpecificationProviderManager
                            .getSpecificationProvider("price").getOrderByPriceDesc());
                    break;
                case "rating":
                    spec = spec.and(courseSpecificationProviderManager
                            .getSpecificationProvider("rating").getOrderByRatingDesc());
                    break;
                default:
                    break;
            }
        }
        return spec;
    }
}
