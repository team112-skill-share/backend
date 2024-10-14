package mate.academy.skillshare.repository.course;

import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import mate.academy.skillshare.model.Course;
import mate.academy.skillshare.repository.SpecificationProvider;
import mate.academy.skillshare.repository.SpecificationProviderManager;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CourseSpecificationProviderManager implements SpecificationProviderManager<Course> {
    private final List<SpecificationProvider<Course>> projectSpecificationProviders;

    @Override
    public SpecificationProvider<Course> getSpecificationProvider(String key) {
        return projectSpecificationProviders.stream()
                .filter(provider -> provider.getKey().equals(key))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(
                        "Can't find correct specification provider for key: " + key));
    }
}
