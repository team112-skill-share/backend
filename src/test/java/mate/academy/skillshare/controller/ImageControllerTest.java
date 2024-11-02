package mate.academy.skillshare.controller;

import static mate.academy.skillshare.util.ImageTestUtil.createTestCreateImageRequestDto;
import static mate.academy.skillshare.util.ImageTestUtil.createTestImage;
import static mate.academy.skillshare.util.ImageTestUtil.createTestImageDto;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import mate.academy.skillshare.config.GlobalSetupExtension;
import mate.academy.skillshare.dto.image.CreateImageRequestDto;
import mate.academy.skillshare.dto.image.ImageDto;
import mate.academy.skillshare.model.Image;
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
public class ImageControllerTest {
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
    @DisplayName("Update image by id")
    @Sql(scripts = {
            "classpath:database/categories/add-category.sql",
            "classpath:database/courses/add-course.sql",
            "classpath:database/images/add-image-for-course.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(scripts = {
            "classpath:database/images/delete-images.sql",
            "classpath:database/courses/delete-courses.sql",
            "classpath:database/categories/delete-categories.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    public void updateImage_GivenValidRequestDto_ShouldReturnImageDto()
            throws Exception {
        //Given
        Image image = createTestImage();
        image.setUrl("upd url");
        CreateImageRequestDto requestDto = createTestCreateImageRequestDto(image);
        ImageDto expected = createTestImageDto(image);
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        //When
        MvcResult result = mockMvc.perform(
                put("/images/1")
                .content(jsonRequest)
                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        //Then
        ImageDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), ImageDto.class
        );
        EqualsBuilder.reflectionEquals(expected, actual);
    }
}
