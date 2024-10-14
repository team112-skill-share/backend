package mate.academy.skillshare.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.skillshare.dto.comment.CommentDto;
import mate.academy.skillshare.dto.comment.CreateCommentRequestDto;
import mate.academy.skillshare.exception.AuthenticationException;
import mate.academy.skillshare.exception.EntityNotFoundException;
import mate.academy.skillshare.mapper.CommentMapper;
import mate.academy.skillshare.model.Article;
import mate.academy.skillshare.model.Comment;
import mate.academy.skillshare.model.User;
import mate.academy.skillshare.repository.article.ArticleRepository;
import mate.academy.skillshare.repository.comment.CommentRepository;
import mate.academy.skillshare.repository.user.UserRepository;
import mate.academy.skillshare.service.CommentService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;

    @Override
    public CommentDto create(Long userId, Long articleId, CreateCommentRequestDto requestDto) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new EntityNotFoundException("Can't find user by id: " + userId));
        Article article = articleRepository.findById(articleId).orElseThrow(() ->
                new EntityNotFoundException("Can't find article by id: " + articleId));
        Comment comment = commentMapper.toModel(requestDto);
        comment.setUser(user);
        comment.setArticle(article);
        return commentMapper.toDto(commentRepository.save(comment));
    }

    @Override
    public List<CommentDto> getAll(Long articleId, Pageable pageable) {
        return commentMapper.toDtoList(
                commentRepository.findAllByArticleId(articleId, pageable).getContent());
    }

    @Override
    public CommentDto update(Long userId, Long id, CreateCommentRequestDto requestDto) {
        Comment comment = commentRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Can't find comment by id: " + id));
        if (!comment.getUser().getId().equals(userId)) {
            throw new AuthenticationException("Comment can be updated only by its creator");
        }
        comment.setComment(requestDto.comment());
        return commentMapper.toDto(commentRepository.save(comment));
    }

    @Override
    public void delete(Long id) {
        commentRepository.deleteById(id);
    }
}
