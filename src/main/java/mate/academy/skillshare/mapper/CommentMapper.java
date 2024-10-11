package mate.academy.skillshare.mapper;

import java.util.List;
import mate.academy.skillshare.config.MapperConfig;
import mate.academy.skillshare.dto.comment.CommentDto;
import mate.academy.skillshare.dto.comment.CreateCommentRequestDto;
import mate.academy.skillshare.model.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface CommentMapper {
    @Mapping(target = "articleId", source = "article.id")
    @Mapping(target = "userId", source = "user.id")
    CommentDto toDto(Comment comment);

    Comment toModel(CreateCommentRequestDto requestDto);

    @Mapping(target = "articleId", source = "article.id")
    @Mapping(target = "userId", source = "user.id")
    List<CommentDto> toDtoList(List<Comment> comments);
}
