package mate.academy.skillshare.controller;

import static mate.academy.skillshare.util.CommentTestUtil.createTestComment;
import static mate.academy.skillshare.util.CommentTestUtil.createTestCommentDto;
import static mate.academy.skillshare.util.CommentTestUtil.createTestCreateCommentRequestDto;
import static mate.academy.skillshare.util.CommentTestUtil.fillExpectedCommentDtoList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import mate.academy.skillshare.config.GlobalSetupExtension;
import mate.academy.skillshare.dto.comment.CommentDto;
import mate.academy.skillshare.dto.comment.CreateCommentRequestDto;
import mate.academy.skillshare.dto.review.ReviewDto;
import mate.academy.skillshare.model.Comment;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(GlobalSetupExtension.class)
public class CommentControllerTest {
    protected static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(
            @Autowired DataSource dataSource,
            @Autowired WebApplicationContext applicationContext
    ) throws SQLException {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
        teardown(dataSource);
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/users/add-user.sql")
            );
        }
    }

    @Test
    @WithUserDetails("bob@example.test")
    @DisplayName("Create a new comment")
    @Sql(scripts = {
            "classpath:database/articles/add-article.sql",
            "classpath:database/subtitles/add-subtitle-for-article.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(scripts = {
            "classpath:database/comments/delete-comments.sql",
            "classpath:database/subtitles/delete-subtitles.sql",
            "classpath:database/articles/delete-articles.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    public void createComment_ValidRequestDto_ShouldCreateNewComment() throws Exception {
        //Given
        Comment comment = createTestComment();
        CreateCommentRequestDto requestDto = createTestCreateCommentRequestDto(comment);
        CommentDto expected = createTestCommentDto(comment);
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        //When
        MvcResult result = mockMvc.perform(
                post("/comments/article/1")
                .content(jsonRequest)
                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andReturn();
        //Then
        CommentDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), CommentDto.class
        );
        assertNotNull(actual);
        assertNotNull(actual.id());
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @Test
    @WithUserDetails("bob@example.test")
    @DisplayName("Retrieve all comments for an article")
    @Sql(scripts = {
            "classpath:database/articles/add-article.sql",
            "classpath:database/subtitles/add-subtitle-for-article.sql",
            "classpath:database/comments/add-two-comments.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(scripts = {
            "classpath:database/comments/delete-comments.sql",
            "classpath:database/subtitles/delete-subtitles.sql",
            "classpath:database/articles/delete-articles.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    public void getAllComments_GivenComments_ShouldReturnComments()
            throws Exception {
        //Given
        List<CommentDto> expected = fillExpectedCommentDtoList();
        //When
        MvcResult result = mockMvc.perform(
                get("/comments/article/1")
                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        //Then
        CommentDto[] actual = objectMapper.readValue(
                result.getResponse().getContentAsByteArray(), CommentDto[].class
        );
        assertEquals(2, actual.length);
        EqualsBuilder.reflectionEquals(expected, Arrays.stream(actual).toList());
    }

    @Test
    @WithUserDetails("bob@example.test")
    @DisplayName("Update comment by id")
    @Sql(scripts = {
            "classpath:database/articles/add-article.sql",
            "classpath:database/subtitles/add-subtitle-for-article.sql",
            "classpath:database/comments/add-two-comments.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(scripts = {
            "classpath:database/comments/delete-comments.sql",
            "classpath:database/subtitles/delete-subtitles.sql",
            "classpath:database/articles/delete-articles.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    public void updateComment_ValidRequestDto_ShouldUpdateComment() throws Exception {
        //Given
        Comment comment = createTestComment();
        comment.setComment("upd comment");
        CreateCommentRequestDto requestDto = createTestCreateCommentRequestDto(comment);
        CommentDto expected = createTestCommentDto(comment);
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        //When
        MvcResult result = mockMvc.perform(
                patch("/comments/1")
                .content(jsonRequest)
                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        //Then
        CommentDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), CommentDto.class
        );
        EqualsBuilder.reflectionEquals(expected, actual);
    }

    @Test
    @WithUserDetails("bob@example.test")
    @DisplayName("Delete comment by id")
    @Sql(scripts = {
            "classpath:database/articles/add-article.sql",
            "classpath:database/subtitles/add-subtitle-for-article.sql",
            "classpath:database/comments/add-two-comments.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(scripts = {
            "classpath:database/comments/delete-comments.sql",
            "classpath:database/subtitles/delete-subtitles.sql",
            "classpath:database/articles/delete-articles.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    public void deleteComment_GivenComments_ShouldDeleteComment() throws Exception {
        //Given
        CommentDto expected = createTestCommentDto(createTestComment());
        //When
        mockMvc.perform(
                delete("/comments/2")
                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent())
                .andReturn();
        MvcResult result = mockMvc.perform(
                get("/comments/article/1")
                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        //Then
        ReviewDto[] actual = objectMapper.readValue(
                result.getResponse().getContentAsByteArray(), ReviewDto[].class
        );
        assertEquals(1, actual.length);
        EqualsBuilder.reflectionEquals(expected, Arrays.stream(actual).findFirst());
    }

    @AfterAll
    static void afterAll(
            @Autowired DataSource dataSource
    ) {
        teardown(dataSource);
    }

    @SneakyThrows
    static void teardown(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/users/delete-users.sql")
            );
        }
    }
}
