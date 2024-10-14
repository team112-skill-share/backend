package mate.academy.skillshare.service;

import java.util.List;
import mate.academy.skillshare.dto.comment.CommentDto;
import mate.academy.skillshare.dto.comment.CreateCommentRequestDto;
import org.springframework.data.domain.Pageable;

public interface CommentService {
    CommentDto create(Long userId, Long articleId, CreateCommentRequestDto requestDto);

    List<CommentDto> getAll(Long articleId, Pageable pageable);

    CommentDto update(Long userId, Long id, CreateCommentRequestDto requestDto);

    void delete(Long id);
}
