package mate.academy.skillshare.controller;

import static mate.academy.skillshare.util.ArticleTestUtil.createTestArticle;
import static mate.academy.skillshare.util.ArticleTestUtil.createTestArticleDto;
import static mate.academy.skillshare.util.ArticleTestUtil.createTestCreateArticleRequestDto;
import static mate.academy.skillshare.util.ArticleTestUtil.fillExpectedArticleCardDtoList;
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
import mate.academy.skillshare.dto.article.ArticleCardDto;
import mate.academy.skillshare.dto.article.ArticleDto;
import mate.academy.skillshare.dto.article.CreateArticleRequestDto;
import mate.academy.skillshare.model.Article;
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
public class ArticleControllerTest {
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
    @DisplayName("Create a new article")
    @Sql(scripts = {
            "classpath:database/subtitles/delete-subtitles.sql",
            "classpath:database/articles/delete-articles.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    public void createArticle_ValidRequestDto_ShouldCreateNewArticle() throws Exception {
        //Given
        Article article = createTestArticle();
        CreateArticleRequestDto requestDto = createTestCreateArticleRequestDto(article);
        ArticleDto expected = createTestArticleDto(article);
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        //When
        MvcResult result = mockMvc.perform(
                post("/articles")
                .content(jsonRequest)
                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andReturn();
        //Then
        ArticleDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), ArticleDto.class
        );
        assertNotNull(actual);
        assertNotNull(actual.id());
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @Test
    @WithMockUser(username = "user")
    @DisplayName("Retrieve all articles")
    @Sql(scripts = {
            "classpath:database/articles/delete-articles.sql",
            "classpath:database/articles/add-two-articles.sql",
            "classpath:database/subtitles/add-two-subtitles.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(scripts = {
            "classpath:database/subtitles/delete-subtitles.sql",
            "classpath:database/articles/delete-articles.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    public void getAllArticles_GivenArticles_ShouldReturnAllArticles()
            throws Exception {
        //Given
        List<ArticleCardDto> expected = fillExpectedArticleCardDtoList();
        //When
        MvcResult result = mockMvc.perform(
                get("/articles")
                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        //Then
        ArticleCardDto[] actual = objectMapper.readValue(
                result.getResponse().getContentAsByteArray(), ArticleCardDto[].class
        );
        assertEquals(2, actual.length);
        EqualsBuilder.reflectionEquals(expected, Arrays.stream(actual).toList());
    }

    @Test
    @WithMockUser(username = "user")
    @DisplayName("Get article by id")
    @Sql(scripts = {
            "classpath:database/articles/add-article.sql",
            "classpath:database/subtitles/add-subtitle-for-article.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(scripts = {
            "classpath:database/subtitles/delete-subtitles.sql",
            "classpath:database/articles/delete-articles.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    public void getArticle_GivenArticle_ShouldReturnArticleDto() throws Exception {
        //Given
        ArticleDto expected = createTestArticleDto(createTestArticle());
        //When
        MvcResult result = mockMvc.perform(
                get("/articles/1")
                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        //Then
        ArticleDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), ArticleDto.class
        );
        EqualsBuilder.reflectionEquals(expected, actual);
    }

    @Test
    @WithMockUser(username = "user")
    @DisplayName("Update article by id")
    @Sql(scripts = {
            "classpath:database/articles/add-article.sql",
            "classpath:database/subtitles/add-subtitle-for-article.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(scripts = {
            "classpath:database/subtitles/delete-subtitles.sql",
            "classpath:database/articles/delete-articles.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    public void updateArticle_ValidRequestDto_ShouldUpdateArticle() throws Exception {
        //Given
        Article article = createTestArticle();
        article.setAuthor("upd author");
        CreateArticleRequestDto requestDto = createTestCreateArticleRequestDto(article);
        ArticleDto expected = createTestArticleDto(article);
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        //When
        MvcResult result = mockMvc.perform(
                put("/articles/1")
                .content(jsonRequest)
                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        //Then
        ArticleDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), ArticleDto.class
        );
        EqualsBuilder.reflectionEquals(expected, actual);
    }

    @Test
    @WithMockUser(username = "user")
    @DisplayName("Delete article by id")
    @Sql(scripts = {
            "classpath:database/articles/add-two-articles.sql",
            "classpath:database/subtitles/add-two-subtitles.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(scripts = {
            "classpath:database/subtitles/delete-subtitles.sql",
            "classpath:database/articles/delete-articles.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    public void deleteArticle_GivenArticle_ShouldDeleteArticle() throws Exception {
        //Given
        List<ArticleCardDto> expected = fillExpectedArticleCardDtoList();
        expected.removeLast();
        //When
        mockMvc.perform(
                delete("/articles/2")
                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent())
                .andReturn();
        MvcResult result = mockMvc.perform(
                get("/articles")
                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        //Then
        ArticleCardDto[] actual = objectMapper.readValue(
                result.getResponse().getContentAsByteArray(), ArticleCardDto[].class
        );
        assertEquals(1, actual.length);
        EqualsBuilder.reflectionEquals(expected, Arrays.stream(actual).collect(Collectors.toSet()));
    }
}
