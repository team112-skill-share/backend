package mate.academy.skillshare.service;

import static mate.academy.skillshare.util.CommentTestUtil.createTestComment;
import static mate.academy.skillshare.util.CommentTestUtil.createTestCommentDto;
import static mate.academy.skillshare.util.CommentTestUtil.createTestCreateCommentRequestDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import mate.academy.skillshare.dto.comment.CommentDto;
import mate.academy.skillshare.dto.comment.CreateCommentRequestDto;
import mate.academy.skillshare.exception.AuthenticationException;
import mate.academy.skillshare.exception.EntityNotFoundException;
import mate.academy.skillshare.mapper.CommentMapper;
import mate.academy.skillshare.model.Comment;
import mate.academy.skillshare.repository.article.ArticleRepository;
import mate.academy.skillshare.repository.comment.CommentRepository;
import mate.academy.skillshare.repository.user.UserRepository;
import mate.academy.skillshare.service.internal.impl.CommentServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private CommentMapper commentMapper;
    @Mock
    private ArticleRepository articleRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CommentServiceImpl commentService;

    @Test
    @DisplayName("Verify that correct CommentDto was returned when calling create() method")
    public void create_WithValidRequestDto_ShouldReturnValidCommentDto() {
        //Given
        Comment comment = createTestComment();
        CreateCommentRequestDto requestDto = createTestCreateCommentRequestDto(comment);
        CommentDto expected = createTestCommentDto(comment);
        Long userId = comment.getUser().getId();
        Long articleId = comment.getArticle().getId();

        when(userRepository.findById(userId)).thenReturn(Optional.of(comment.getUser()));
        when(articleRepository.findById(articleId)).thenReturn(Optional.of(comment.getArticle()));
        when(commentMapper.toModel(requestDto)).thenReturn(comment);
        when(commentRepository.save(comment)).thenReturn(comment);
        when(commentMapper.toDto(comment)).thenReturn(expected);
        //When
        CommentDto actual = commentService.create(userId, articleId, requestDto);
        //Then
        assertEquals(expected, actual);

        verify(userRepository, times(1)).findById(userId);
        verify(articleRepository, times(1)).findById(articleId);
        verify(commentMapper, times(1)).toModel(requestDto);
        verify(commentRepository, times(1)).save(comment);
        verify(commentMapper, times(1)).toDto(comment);
        verifyNoMoreInteractions(
                userRepository, articleRepository, commentMapper, commentRepository);
    }

    @Test
    @DisplayName(
            "Verify that exception is thrown when calling create() method with invalid user id")
    public void create_WithInvalidUserId_ShouldThrowException() {
        //Given
        Comment comment = createTestComment();
        CreateCommentRequestDto requestDto = createTestCreateCommentRequestDto(comment);
        Long userId = 2L;
        Long articleId = comment.getArticle().getId();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        //When
        Exception exception = assertThrows(
                EntityNotFoundException.class,
                () -> commentService.create(userId, articleId, requestDto)
        );
        //Then
        String expected = "Can't find user by id: " + userId;
        String actual = exception.getMessage();
        assertEquals(expected, actual);

        verify(userRepository, times(1)).findById(userId);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    @DisplayName(
            "Verify that exception is thrown when calling create() method with invalid course id")
    public void create_WithInvalidCourseId_ShouldThrowException() {
        //Given
        Comment comment = createTestComment();
        CreateCommentRequestDto requestDto = createTestCreateCommentRequestDto(comment);
        Long userId = comment.getUser().getId();
        Long articleId = 2L;

        when(userRepository.findById(userId)).thenReturn(Optional.of(comment.getUser()));
        when(articleRepository.findById(articleId)).thenReturn(Optional.empty());
        //When
        Exception exception = assertThrows(
                EntityNotFoundException.class,
                () -> commentService.create(userId, articleId, requestDto)
        );
        //Then
        String expected = "Can't find article by id: " + articleId;
        String actual = exception.getMessage();
        assertEquals(expected, actual);

        verify(userRepository, times(1)).findById(userId);
        verify(articleRepository, times(1)).findById(articleId);
        verifyNoMoreInteractions(userRepository, articleRepository);
    }

    @Test
    @DisplayName("Verify that correct Comment list was returned when calling getAll() method")
    public void getAll_WithValidComment_ShouldReturnCorrectCommentDtoList() {
        //Given
        Comment comment = createTestComment();
        CommentDto commentDto = createTestCommentDto(comment);
        Pageable pageable = PageRequest.of(0, 20);
        Page<Comment> coursePage = new PageImpl<>(List.of(comment), pageable, 1);
        List<CommentDto> expected = List.of(commentDto);
        Long articleId = comment.getArticle().getId();

        when(commentRepository.findAllByArticleId(articleId, pageable)).thenReturn(coursePage);
        when(commentMapper.toDtoList(coursePage.getContent())).thenReturn(expected);
        //When
        List<CommentDto> actual = commentService.getAll(articleId, pageable);
        //Then
        assertEquals(expected, actual);

        verify(commentRepository, times(1)).findAllByArticleId(articleId, pageable);
        verify(commentMapper, times(1)).toDtoList(coursePage.getContent());
        verifyNoMoreInteractions(commentRepository, commentMapper);
    }

    @Test
    @DisplayName("Verify that correct CommentDto was returned when calling update() method")
    public void update_WithValidRequestDto_ShouldReturnValidCommentDto() {
        //Given
        Comment comment = createTestComment();
        Comment updatedComment = createTestComment();
        updatedComment.setComment("upd comment");
        CreateCommentRequestDto requestDto = createTestCreateCommentRequestDto(updatedComment);
        CommentDto expected = createTestCommentDto(updatedComment);
        Long id = expected.id();

        when(commentRepository.findById(id)).thenReturn(Optional.of(comment));
        when(commentRepository.save(comment)).thenReturn(comment);
        when(commentMapper.toDto(comment)).thenReturn(expected);
        //When
        CommentDto actual = commentService.update(comment.getUser().getId(), id, requestDto);
        //Then
        assertEquals(expected, actual);

        verify(commentRepository, times(1)).findById(id);
        verify(commentRepository, times(1)).save(comment);
        verify(commentMapper, times(1)).toDto(comment);
        verifyNoMoreInteractions(commentRepository, commentMapper);
    }

    @Test
    @DisplayName(
            "Verify that exception is thrown when calling update() method with invalid id")
    public void update_WithInvalidId_ShouldThrowException() {
        //Given
        Comment comment = createTestComment();
        CreateCommentRequestDto requestDto = createTestCreateCommentRequestDto(comment);
        Long userId = comment.getUser().getId();
        Long id = 2L;

        when(commentRepository.findById(id)).thenReturn(Optional.empty());
        //When
        Exception exception = assertThrows(
                EntityNotFoundException.class,
                () -> commentService.update(userId, id, requestDto)
        );
        //Then
        String expected = "Can't find comment by id: " + id;
        String actual = exception.getMessage();
        assertEquals(expected, actual);

        verify(commentRepository, times(1)).findById(id);
        verifyNoMoreInteractions(commentRepository);
    }

    @Test
    @DisplayName(
            "Verify that exception is thrown when calling update() method with invalid user id")
    public void update_WithInvalidUserId_ShouldThrowException() {
        //Given
        Comment comment = createTestComment();
        CreateCommentRequestDto requestDto = createTestCreateCommentRequestDto(comment);
        Long userId = 2L;
        Long id = comment.getId();

        when(commentRepository.findById(id)).thenReturn(Optional.of(comment));
        //When
        Exception exception = assertThrows(
                AuthenticationException.class,
                () -> commentService.update(userId, id, requestDto)
        );
        //Then
        String expected = "Comment can be updated only by its creator";
        String actual = exception.getMessage();
        assertEquals(expected, actual);

        verify(commentRepository, times(1)).findById(id);
        verifyNoMoreInteractions(commentRepository);
    }
}
