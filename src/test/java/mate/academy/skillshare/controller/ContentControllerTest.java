package mate.academy.skillshare.controller;

import static mate.academy.skillshare.util.ContentTestUtil.createTestContent;
import static mate.academy.skillshare.util.ContentTestUtil.createTestContentDto;
import static mate.academy.skillshare.util.ContentTestUtil.createTestCreateContentRequestDto;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import mate.academy.skillshare.config.GlobalSetupExtension;
import mate.academy.skillshare.dto.content.ContentDto;
import mate.academy.skillshare.dto.content.CreateContentRequestDto;
import mate.academy.skillshare.model.Content;
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
public class ContentControllerTest {
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
    @DisplayName("Update content by id")
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
    public void updateUserEmail_GivenValidRequestDto_ShouldReturnUserResponseDto()
            throws Exception {
        //Given
        Content content = createTestContent();
        content.setText("upd text");
        CreateContentRequestDto requestDto = createTestCreateContentRequestDto(content);
        ContentDto expected = createTestContentDto(content);
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        //When
        MvcResult result = mockMvc.perform(
                put("/contents/1")
                .content(jsonRequest)
                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        //Then
        ContentDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), ContentDto.class
        );
        EqualsBuilder.reflectionEquals(expected, actual);
    }
}
