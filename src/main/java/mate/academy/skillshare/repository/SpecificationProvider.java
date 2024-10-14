package mate.academy.skillshare.repository;

import org.springframework.data.jpa.domain.Specification;

public interface SpecificationProvider<T> {
    String getKey();

    default Specification<T> getSpecification(String param) {
        throw new UnsupportedOperationException("Can't get specification with such "
                + "input parameter. Method is not implemented");
    }

    default Specification<T> getSpecification(Long param) {
        throw new UnsupportedOperationException("Can't get specification with such "
                + "input parameter. Method is not implemented");
    }

    default Specification<T> getOrderByPriceAsc() {
        throw new UnsupportedOperationException("Can't get specification. "
                + "Method is not implemented");
    }

    default Specification<T> getOrderByPriceDesc() {
        throw new UnsupportedOperationException("Can't get specification. "
                + "Method is not implemented");
    }

    default Specification<T> getOrderByRatingDesc() {
        throw new UnsupportedOperationException("Can't get specification. "
                + "Method is not implemented");
    }
}
