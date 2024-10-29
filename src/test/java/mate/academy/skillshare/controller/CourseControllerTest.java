package mate.academy.skillshare.controller;

import static mate.academy.skillshare.util.CourseTestUtil.createTestCourse;
import static mate.academy.skillshare.util.CourseTestUtil.createTestCourseDto;
import static mate.academy.skillshare.util.CourseTestUtil.createTestCreateCourseRequestDto;
import static mate.academy.skillshare.util.CourseTestUtil.fillExpectedCourseCardDtoList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import mate.academy.skillshare.config.GlobalSetupExtension;
import mate.academy.skillshare.dto.course.CourseCardDto;
import mate.academy.skillshare.dto.course.CourseDto;
import mate.academy.skillshare.dto.course.CourseSearchParameters;
import mate.academy.skillshare.dto.course.CreateCourseRequestDto;
import mate.academy.skillshare.model.Course;
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
public class CourseControllerTest {
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
    @DisplayName("Create a new course")
    @Sql(scripts = {
            "classpath:database/categories/add-category.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(scripts = {
            "classpath:database/contents/delete-contents.sql",
            "classpath:database/courses/delete-courses.sql",
            "classpath:database/categories/delete-categories.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    public void createCourse_ValidRequestDto_ShouldCreateNewCourse() throws Exception {
        //Given
        Course course = createTestCourse();
        CreateCourseRequestDto requestDto = createTestCreateCourseRequestDto(course);
        CourseDto expected = createTestCourseDto(course);
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        //When
        MvcResult result = mockMvc.perform(
                post("/courses")
                .content(jsonRequest)
                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andReturn();
        //Then
        CourseDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), CourseDto.class
        );
        assertNotNull(actual);
        assertNotNull(actual.id());
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @Test
    @WithMockUser(username = "user")
    @DisplayName("Retrieve all courses")
    @Sql(scripts = {
            "classpath:database/categories/add-two-categories.sql",
            "classpath:database/courses/add-two-courses.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(scripts = {
            "classpath:database/courses/delete-courses.sql",
            "classpath:database/categories/delete-categories.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    public void getAllCourses_GivenCourses_ShouldReturnAllCourses()
            throws Exception {
        //Given
        List<CourseCardDto> expected = fillExpectedCourseCardDtoList();
        //When
        MvcResult result = mockMvc.perform(
                get("/courses")
                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        //Then
        CourseCardDto[] actual = objectMapper.readValue(
                result.getResponse().getContentAsByteArray(), CourseCardDto[].class
        );
        assertEquals(2, actual.length);
        EqualsBuilder.reflectionEquals(expected, Arrays.stream(actual).toList());
    }

    @Test
    @WithMockUser(username = "user")
    @DisplayName("Search for specific courses")
    @Sql(scripts = {
            "classpath:database/categories/add-two-categories.sql",
            "classpath:database/courses/add-two-courses.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(scripts = {
            "classpath:database/courses/delete-courses.sql",
            "classpath:database/categories/delete-categories.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    public void searchCourses_GivenCourses_ShouldReturnOneCourse()
            throws Exception {
        //Given
        List<CourseCardDto> expected = fillExpectedCourseCardDtoList();
        expected.removeLast();
        CourseSearchParameters parameters = new CourseSearchParameters(null, "author1", null);
        String jsonRequest = objectMapper.writeValueAsString(parameters);
        //When
        MvcResult result = mockMvc.perform(
                get("/courses/search")
                .content(jsonRequest)
                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        //Then
        CourseCardDto[] actual = objectMapper.readValue(
                result.getResponse().getContentAsByteArray(), CourseCardDto[].class
        );
        assertEquals(1, actual.length);
        EqualsBuilder.reflectionEquals(expected, Arrays.stream(actual).toList());
    }

    @Test
    @WithMockUser(username = "user")
    @DisplayName("Get course by id")
    @Sql(scripts = {
            "classpath:database/categories/add-category.sql",
            "classpath:database/courses/add-course.sql",
            "classpath:database/contents/add-content-for-course.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(scripts = {
            "classpath:database/contents/delete-contents.sql",
            "classpath:database/courses/delete-courses.sql",
            "classpath:database/categories/delete-categories.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    public void getCourse_GivenCourse_ShouldReturnCourseDto() throws Exception {
        //Given
        CourseDto expected = createTestCourseDto(createTestCourse());
        //When
        MvcResult result = mockMvc.perform(
                get("/courses/1")
                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        //Then
        CourseDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), CourseDto.class
        );
        EqualsBuilder.reflectionEquals(expected, actual);
    }

    @Test
    @WithMockUser(username = "user")
    @DisplayName("Update course by id")
    @Sql(scripts = {
            "classpath:database/categories/add-category.sql",
            "classpath:database/courses/add-course.sql",
            "classpath:database/contents/add-content-for-course.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(scripts = {
            "classpath:database/contents/delete-contents.sql",
            "classpath:database/courses/delete-courses.sql",
            "classpath:database/categories/delete-categories.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    public void updateCourse_ValidRequestDto_ShouldUpdateCourse() throws Exception {
        //Given
        Course course = createTestCourse();
        course.setAuthor("upd author");
        CreateCourseRequestDto requestDto = createTestCreateCourseRequestDto(course);
        CourseDto expected = createTestCourseDto(course);
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        //When
        MvcResult result = mockMvc.perform(
                put("/courses/1")
                .content(jsonRequest)
                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        //Then
        CourseDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), CourseDto.class
        );
        EqualsBuilder.reflectionEquals(expected, actual);
    }

    @Test
    @WithMockUser(username = "user")
    @DisplayName("Delete course by id")
    @Sql(scripts = {
            "classpath:database/categories/add-two-categories.sql",
            "classpath:database/courses/add-two-courses.sql",
            "classpath:database/contents/add-two-contents.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(scripts = {
            "classpath:database/contents/delete-contents.sql",
            "classpath:database/courses/delete-courses.sql",
            "classpath:database/categories/delete-categories.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    public void deleteCourse_GivenCourse_ShouldDeleteCourse() throws Exception {
        //Given
        List<CourseCardDto> expected = fillExpectedCourseCardDtoList();
        expected.removeLast();
        //When
        mockMvc.perform(
                delete("/courses/2")
                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent())
                .andReturn();
        MvcResult result = mockMvc.perform(
                get("/courses")
                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        //Then
        CourseCardDto[] actual = objectMapper.readValue(
                result.getResponse().getContentAsByteArray(), CourseCardDto[].class
        );
        assertEquals(1, actual.length);
        EqualsBuilder.reflectionEquals(expected, Arrays.stream(actual).collect(Collectors.toSet()));
    }
}
