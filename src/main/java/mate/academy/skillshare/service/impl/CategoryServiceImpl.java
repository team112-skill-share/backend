package mate.academy.skillshare.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.skillshare.dto.category.CreateCategoryRequestDto;
import mate.academy.skillshare.mapper.CategoryMapper;
import mate.academy.skillshare.model.Category;
import mate.academy.skillshare.repository.category.CategoryRepository;
import mate.academy.skillshare.service.CategoryService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public Category create(CreateCategoryRequestDto requestDto) {
        return categoryRepository.save(categoryMapper.toModel(requestDto));
    }

    @Override
    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    @Override
    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }
}
