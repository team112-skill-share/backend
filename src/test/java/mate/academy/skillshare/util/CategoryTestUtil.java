package mate.academy.skillshare.util;

import java.util.ArrayList;
import java.util.List;
import mate.academy.skillshare.dto.category.CreateCategoryRequestDto;
import mate.academy.skillshare.model.Category;

public class CategoryTestUtil {
    public static Category createTestCategory() {
        Category category = new Category();
        category.setId(1L);
        category.setName("Category");
        return category;
    }

    public static CreateCategoryRequestDto createTestCreateCategoryRequestDto(Category category) {
        return new CreateCategoryRequestDto(category.getName());
    }

    public static List<Category> fillExpectedCategoryList() {
        List<Category> categoryList = new ArrayList<>();
        categoryList.add(new Category().setId(1L).setName("Category1"));
        categoryList.add(new Category().setId(2L).setName("Category2"));
        return categoryList;
    }
}
