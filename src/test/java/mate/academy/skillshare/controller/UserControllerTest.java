package mate.academy.skillshare.controller;

import static mate.academy.skillshare.util.UserTestUtil.createTestUser;
import static mate.academy.skillshare.util.UserTestUtil.createTestUserEmailChangeRequestDto;
import static mate.academy.skillshare.util.UserTestUtil.createTestUserPasswordChangeRequestDto;
import static mate.academy.skillshare.util.UserTestUtil.createTestUserRegistrationRequestDto;
import static mate.academy.skillshare.util.UserTestUtil.createTestUserResponseDto;
import static mate.academy.skillshare.util.UserTestUtil.createTestUserResponseDtoWithCourse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import mate.academy.skillshare.config.GlobalSetupExtension;
import mate.academy.skillshare.dto.user.UserEmailChangeRequestDto;
import mate.academy.skillshare.dto.user.UserPasswordChangeRequestDto;
import mate.academy.skillshare.dto.user.UserRegistrationRequestDto;
import mate.academy.skillshare.dto.user.UserResponseDto;
import mate.academy.skillshare.model.User;
import mate.academy.skillshare.repository.user.UserRepository;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(GlobalSetupExtension.class)
public class UserControllerTest {
    protected static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepository;
    @MockBean
    private PasswordEncoder passwordEncoder;

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
    @WithUserDetails("bob@example.test")
    @DisplayName("Get current user's info")
    @Sql(scripts = {
            "classpath:database/users/add-user.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(scripts = {
            "classpath:database/users/delete-users.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    public void getUserInfo_GivenUser_ShouldReturnUserResponseDto() throws Exception {
        //Given
        User user = createTestUser();
        UserResponseDto expected = createTestUserResponseDto(user);
        //When
        MvcResult result = mockMvc.perform(
                get("/users/me")
                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        //Then
        UserResponseDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), UserResponseDto.class
        );
        EqualsBuilder.reflectionEquals(expected, actual);
    }

    @Test
    @WithUserDetails("bob@example.test")
    @DisplayName("Update current user's info")
    @Sql(scripts = {
            "classpath:database/users/add-user.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(scripts = {
            "classpath:database/users/delete-users.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    public void updateUserInfo_GivenValidRequestDto_ShouldReturnUserResponseDto() throws Exception {
        //Given
        User user = createTestUser();
        user.setFullName("Bobby");
        UserRegistrationRequestDto requestDto = createTestUserRegistrationRequestDto(user);
        UserResponseDto expected = createTestUserResponseDto(user);
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        //When
        MvcResult result = mockMvc.perform(
                put("/users/me")
                .content(jsonRequest)
                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        //Then
        UserResponseDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), UserResponseDto.class
        );
        EqualsBuilder.reflectionEquals(expected, actual);
    }

    @Test
    @WithUserDetails("bob@example.test")
    @DisplayName("Update current user's password")
    @Sql(scripts = {
            "classpath:database/users/add-user.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(scripts = {
            "classpath:database/users/delete-users.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    public void updateUserPassword_GivenValidRequestDto_ShouldReturnUserResponseDto()
            throws Exception {
        // Given
        User user = userRepository.findByEmail("bob@example.test").get();

        when(passwordEncoder.matches("Bobster1", user.getPassword())).thenReturn(true);
        when(passwordEncoder.encode("NEWpasswor3")).thenReturn("encodedNewPassword");

        UserPasswordChangeRequestDto requestDto = createTestUserPasswordChangeRequestDto(user);
        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(
                patch("/users/me/password")
                .content(jsonRequest)
                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        // Then
        User updatedUser = userRepository.findById(user.getId()).get();

        assertEquals("encodedNewPassword", updatedUser.getPassword());
        assertEquals(user.getEmail(), updatedUser.getEmail());

        UserResponseDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), UserResponseDto.class);
        assertEquals(user.getId(), actual.id());
        assertEquals(user.getEmail(), actual.email());
    }

    @Test
    @WithUserDetails("bob@example.test")
    @DisplayName("Update current user's email")
    @Sql(scripts = {
            "classpath:database/users/add-user.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(scripts = {
            "classpath:database/users/delete-users.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    public void updateUserEmail_GivenValidRequestDto_ShouldReturnUserResponseDto()
            throws Exception {
        //Given
        User user = createTestUser();
        user.setEmail("bobby@example.test");
        UserEmailChangeRequestDto requestDto = createTestUserEmailChangeRequestDto();
        UserResponseDto expected = createTestUserResponseDto(user);
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        //When
        MvcResult result = mockMvc.perform(
                patch("/users/me/email")
                .content(jsonRequest)
                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        //Then
        UserResponseDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), UserResponseDto.class
        );
        EqualsBuilder.reflectionEquals(expected, actual);
    }

    @Test
    @WithUserDetails("bob@example.test")
    @DisplayName("Add a course to favourites")
    @Sql(scripts = {
            "classpath:database/users/add-user.sql",
            "classpath:database/categories/add-category.sql",
            "classpath:database/courses/add-course.sql",
            "classpath:database/contents/add-content-for-course.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(scripts = {
            "classpath:database/users_courses/remove-courses-from-favourites.sql",
            "classpath:database/contents/delete-contents.sql",
            "classpath:database/courses/delete-courses.sql",
            "classpath:database/categories/delete-categories.sql",
            "classpath:database/users/delete-users.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    public void addFavourite_ValidCourseId_ShouldAddCourseToFavourites() throws Exception {
        //Given
        UserResponseDto expected = createTestUserResponseDtoWithCourse(createTestUser());
        //When
        MvcResult result = mockMvc.perform(
                post("/users/me/favourites/1")
                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        //Then
        UserResponseDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), UserResponseDto.class
        );
        EqualsBuilder.reflectionEquals(expected, actual);
    }

    @Test
    @WithUserDetails("bob@example.test")
    @DisplayName("Remove a course from favourites")
    @Sql(scripts = {
            "classpath:database/users/add-user.sql",
            "classpath:database/categories/add-category.sql",
            "classpath:database/courses/add-course.sql",
            "classpath:database/contents/add-content-for-course.sql",
            "classpath:database/users_courses/add-course-to-favourites.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(scripts = {
            "classpath:database/users_courses/remove-courses-from-favourites.sql",
            "classpath:database/contents/delete-contents.sql",
            "classpath:database/courses/delete-courses.sql",
            "classpath:database/categories/delete-categories.sql",
            "classpath:database/users/delete-users.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    public void removeFavourite_ValidRequestDto_ShouldRemoveCourseFromFavourites()
            throws Exception {
        //Given
        UserResponseDto expected = createTestUserResponseDto(createTestUser());
        //When
        MvcResult result = mockMvc.perform(
                delete("/users/me/favourites/1")
                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        //Then
        UserResponseDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), UserResponseDto.class
        );
        EqualsBuilder.reflectionEquals(expected, actual);
    }
}
