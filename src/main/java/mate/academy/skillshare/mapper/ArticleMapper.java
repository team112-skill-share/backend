package mate.academy.skillshare.mapper;

import mate.academy.skillshare.config.MapperConfig;
import mate.academy.skillshare.dto.article.CreateArticleRequestDto;
import mate.academy.skillshare.model.Article;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface ArticleMapper {
    Article toModel(CreateArticleRequestDto requestDto);

    @Mapping(target = "id", ignore = true)
    Article updateFromRequest(CreateArticleRequestDto requestDto, @MappingTarget Article article);
}
