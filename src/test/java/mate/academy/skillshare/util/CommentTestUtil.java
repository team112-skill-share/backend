package mate.academy.skillshare.util;

import static mate.academy.skillshare.util.ArticleTestUtil.createTestArticle;
import static mate.academy.skillshare.util.UserTestUtil.createTestUser;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import mate.academy.skillshare.dto.comment.CommentDto;
import mate.academy.skillshare.dto.comment.CreateCommentRequestDto;
import mate.academy.skillshare.model.Comment;

public class CommentTestUtil {
    public static Comment createTestComment() {
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setArticle(createTestArticle());
        comment.setUser(createTestUser());
        comment.setComment("comment1");
        comment.setTimestamp(LocalDateTime.of(2024, 10, 31, 18, 0, 0));
        return comment;
    }

    public static CommentDto createTestCommentDto(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getUser().getId(),
                comment.getComment(),
                comment.getTimestamp()
        );
    }

    public static CreateCommentRequestDto createTestCreateCommentRequestDto(Comment comment) {
        return new CreateCommentRequestDto(
                comment.getComment()
        );
    }

    public static List<CommentDto> fillExpectedCommentDtoList() {
        List<CommentDto> commentList = new ArrayList<>();
        commentList.add(new CommentDto(1L, 1L, "comment1",
                LocalDateTime.of(2024, 10, 31, 18, 0, 0)));
        commentList.add(new CommentDto(2L, 1L, "comment2",
                LocalDateTime.of(2024, 10, 31, 18, 0, 0)));
        return commentList;
    }
}
