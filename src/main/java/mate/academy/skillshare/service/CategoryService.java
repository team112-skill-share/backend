package mate.academy.skillshare.service;

import java.util.List;
import mate.academy.skillshare.dto.category.CreateCategoryRequestDto;
import mate.academy.skillshare.model.Category;

public interface CategoryService {
    Category create(CreateCategoryRequestDto requestDto);

    List<Category> getAll();

    void delete(Long id);
}
