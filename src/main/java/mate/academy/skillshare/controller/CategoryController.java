package mate.academy.skillshare.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.skillshare.dto.category.CreateCategoryRequestDto;
import mate.academy.skillshare.model.Category;
import mate.academy.skillshare.service.internal.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Category managing", description = "Endpoints for managing categories")
@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;

    @Operation(summary = "Create a category", description = "Create a new category")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Category createCategory(@RequestBody @Valid CreateCategoryRequestDto requestDto) {
        return categoryService.create(requestDto);
    }

    @Operation(summary = "Retrieve categories", description = "Retrieve all categories")
    @GetMapping
    public List<Category> getAllCategories() {
        return categoryService.getAll();
    }

    @Operation(summary = "Delete category", description = "Delete a category by id")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable Long id) {
        categoryService.delete(id);
    }
}
