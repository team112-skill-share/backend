package mate.academy.skillshare.controller;

import static mate.academy.skillshare.util.CategoryTestUtil.createTestCategory;
import static mate.academy.skillshare.util.CategoryTestUtil.createTestCreateCategoryRequestDto;
import static mate.academy.skillshare.util.CategoryTestUtil.fillExpectedCategoryList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;
import mate.academy.skillshare.config.GlobalSetupExtension;
import mate.academy.skillshare.dto.category.CreateCategoryRequestDto;
import mate.academy.skillshare.model.Category;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(GlobalSetupExtension.class)
public class CategoryControllerTest {
    protected static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(
            @Autowired WebApplicationContext applicationContext
    ) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithMockUser(username = "user")
    @DisplayName("Create a new category")
    @Sql(scripts = {
            "classpath:database/categories/delete-categories.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    public void createCategory_ValidRequestDto_ShouldCreateNewCategory() throws Exception {
        //Given
        Category expected = createTestCategory();
        CreateCategoryRequestDto requestDto = createTestCreateCategoryRequestDto(expected);
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        //When
        MvcResult result = mockMvc.perform(
                post("/categories")
                .content(jsonRequest)
                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andReturn();
        //Then
        Category actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), Category.class
        );
        assertNotNull(actual);
        assertNotNull(actual.getId());
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @Test
    @WithMockUser(username = "user")
    @DisplayName("Retrieve all categories")
    @Sql(scripts = {
            "classpath:database/categories/add-two-categories.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(scripts = {
            "classpath:database/categories/delete-categories.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    public void getAllCategories_GivenCategories_ShouldReturnAllCategories()
            throws Exception {
        //Given
        List<Category> expected = fillExpectedCategoryList();
        //When
        MvcResult result = mockMvc.perform(
                get("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        //Then
        Category[] actual = objectMapper.readValue(
                result.getResponse().getContentAsByteArray(), Category[].class
        );
        assertEquals(2, actual.length);
        EqualsBuilder.reflectionEquals(expected, Arrays.stream(actual).toList());
    }

    @Test
    @WithMockUser(username = "user")
    @DisplayName("Delete category by id")
    @Sql(scripts = {
            "classpath:database/categories/add-two-categories.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(scripts = {
            "classpath:database/categories/delete-categories.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    public void deleteCategory_GivenCategories_ShouldDeleteCategory() throws Exception {
        //Given
        List<Category> expected = fillExpectedCategoryList();
        expected.removeLast();
        //When
        mockMvc.perform(
                delete("/categories/2")
                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent())
                .andReturn();
        MvcResult result = mockMvc.perform(
                get("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        //Then
        Category[] actual = objectMapper.readValue(
                result.getResponse().getContentAsByteArray(), Category[].class
        );
        assertEquals(1, actual.length);
        EqualsBuilder.reflectionEquals(expected, Arrays.stream(actual).toList());
    }
}
