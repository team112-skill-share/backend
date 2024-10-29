package mate.academy.skillshare.service;

import static mate.academy.skillshare.util.CategoryTestUtil.createTestCategory;
import static mate.academy.skillshare.util.CategoryTestUtil.createTestCreateCategoryRequestDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import mate.academy.skillshare.dto.category.CreateCategoryRequestDto;
import mate.academy.skillshare.exception.InvalidDataException;
import mate.academy.skillshare.mapper.CategoryMapper;
import mate.academy.skillshare.model.Category;
import mate.academy.skillshare.repository.category.CategoryRepository;
import mate.academy.skillshare.service.internal.impl.CategoryServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    @DisplayName("Verify that correct Category was returned when calling create() method")
    public void create_WithValidRequestDto_ShouldReturnValidCategory() {
        //Given
        Category expected = createTestCategory();
        CreateCategoryRequestDto requestDto = createTestCreateCategoryRequestDto(expected);

        when(categoryRepository.findByName(requestDto.name())).thenReturn(Optional.empty());
        when(categoryMapper.toModel(requestDto)).thenReturn(expected);
        when(categoryRepository.save(expected)).thenReturn(expected);
        //When
        Category actual = categoryService.create(requestDto);
        //Then
        assertEquals(expected, actual);

        verify(categoryRepository, times(1)).findByName(requestDto.name());
        verify(categoryMapper, times(1)).toModel(requestDto);
        verify(categoryRepository, times(1)).save(expected);
        verifyNoMoreInteractions(categoryRepository, categoryMapper);
    }

    @Test
    @DisplayName("""
            Verify that exception is thrown when calling create() method
            and category with such name already exists""")
    public void create_WithExistingName_ShouldThrowException() {
        //Given
        Category category = createTestCategory();
        CreateCategoryRequestDto requestDto = createTestCreateCategoryRequestDto(category);

        when(categoryRepository.findByName(category.getName())).thenReturn(Optional.of(category));
        //When
        Exception exception = assertThrows(
                InvalidDataException.class,
                () -> categoryService.create(requestDto)
        );
        //Then
        String expected = "This category already exists";
        String actual = exception.getMessage();
        assertEquals(expected, actual);

        verify(categoryRepository, times(1)).findByName(category.getName());
        verifyNoMoreInteractions(categoryRepository);
    }

    @Test
    @DisplayName("Verify that correct Category list was returned when calling getAll() method")
    public void getAll_WithValidRequest_ShouldReturnCorrectCategoryList() {
        //Given
        List<Category> expected = List.of(createTestCategory());

        when(categoryRepository.findAll()).thenReturn(expected);
        //When
        List<Category> actual = categoryService.getAll();
        //Then
        assertEquals(expected, actual);

        verify(categoryRepository, times(1)).findAll();
        verifyNoMoreInteractions(categoryRepository);
    }
}
