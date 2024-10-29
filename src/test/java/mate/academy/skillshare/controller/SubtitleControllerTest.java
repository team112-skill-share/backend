package mate.academy.skillshare.controller;

import static mate.academy.skillshare.util.SubtitleTestUtil.createTestCreateSubtitleRequestDto;
import static mate.academy.skillshare.util.SubtitleTestUtil.createTestSubtitle;
import static mate.academy.skillshare.util.SubtitleTestUtil.createTestSubtitleDto;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import mate.academy.skillshare.config.GlobalSetupExtension;
import mate.academy.skillshare.dto.subtitle.CreateSubtitleRequestDto;
import mate.academy.skillshare.dto.subtitle.SubtitleDto;
import mate.academy.skillshare.model.Subtitle;
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
public class SubtitleControllerTest {
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
    @DisplayName("Update subtitle by id")
    @Sql(scripts = {
            "classpath:database/articles/add-article.sql",
            "classpath:database/subtitles/add-subtitle-for-article.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(scripts = {
            "classpath:database/subtitles/delete-subtitles.sql",
            "classpath:database/courses/delete-courses.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    public void updateUserEmail_GivenValidRequestDto_ShouldReturnUserResponseDto()
            throws Exception {
        //Given
        Subtitle subtitle = createTestSubtitle();
        subtitle.setSubtitle("upd subtitle");
        CreateSubtitleRequestDto requestDto = createTestCreateSubtitleRequestDto(subtitle);
        SubtitleDto expected = createTestSubtitleDto(subtitle);
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        //When
        MvcResult result = mockMvc.perform(
                put("/subtitles/1")
                .content(jsonRequest)
                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        //Then
        SubtitleDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), SubtitleDto.class
        );
        EqualsBuilder.reflectionEquals(expected, actual);
    }
}
