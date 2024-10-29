package mate.academy.skillshare.mapper;

import mate.academy.skillshare.config.MapperConfig;
import mate.academy.skillshare.dto.content.ContentDto;
import mate.academy.skillshare.dto.content.CreateContentRequestDto;
import mate.academy.skillshare.model.Content;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface ContentMapper {
    ContentDto toDto(Content content);

    Content toModel(CreateContentRequestDto requestDto);
}
