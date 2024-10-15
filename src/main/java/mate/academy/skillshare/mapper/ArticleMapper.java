package mate.academy.skillshare.mapper;

import java.util.List;
import mate.academy.skillshare.config.MapperConfig;
import mate.academy.skillshare.dto.article.ArticleDto;
import mate.academy.skillshare.dto.article.CreateArticleRequestDto;
import mate.academy.skillshare.model.Article;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class, uses = CommentMapper.class)
public interface ArticleMapper {
    ArticleDto toDto(Article article);

    Article toModel(CreateArticleRequestDto requestDto);

    @Mapping(target = "id", ignore = true)
    Article updateFromRequest(CreateArticleRequestDto requestDto, @MappingTarget Article article);

    List<ArticleDto> toDtoList(List<Article> articles);
}
