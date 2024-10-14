package mate.academy.skillshare.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.skillshare.dto.comment.CommentDto;
import mate.academy.skillshare.dto.comment.CreateCommentRequestDto;
import mate.academy.skillshare.model.User;
import mate.academy.skillshare.service.CommentService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Comment managing", description = "Endpoints for managing comments")
@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {
    private final CommentService commentService;

    @Operation(summary = "Create a comment", description = "Create a new comment")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/article/{articleId}")
    public CommentDto createComment(
            Authentication authentication,
            @PathVariable Long articleId,
            @RequestBody @Valid CreateCommentRequestDto requestDto
    ) {
        User user = (User) authentication.getPrincipal();
        return commentService.create(user.getId(), articleId, requestDto);
    }

    @Operation(summary = "Retrieve comments", description = "Retrieve all comments for an article")
    @GetMapping("/article/{articleId}")
    public List<CommentDto> getAllComments(
            @PathVariable Long articleId,
            @ParameterObject @PageableDefault Pageable pageable) {
        return commentService.getAll(articleId, pageable);
    }

    @Operation(summary = "Update a comment", description = "Update a comment by id")
    @PatchMapping("/{id}")
    public CommentDto updateComment(Authentication authentication,
                                    @PathVariable Long id,
                                    @RequestBody @Valid CreateCommentRequestDto requestDto) {
        User user = (User) authentication.getPrincipal();
        return commentService.update(user.getId(), id, requestDto);
    }

    @Operation(summary = "Delete a comment", description = "Delete a comment by id")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteComment(@PathVariable Long id) {
        commentService.delete(id);
    }
}
