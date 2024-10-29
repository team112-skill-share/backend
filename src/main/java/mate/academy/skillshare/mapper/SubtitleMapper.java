package mate.academy.skillshare.mapper;

import mate.academy.skillshare.config.MapperConfig;
import mate.academy.skillshare.dto.subtitle.CreateSubtitleRequestDto;
import mate.academy.skillshare.dto.subtitle.SubtitleDto;
import mate.academy.skillshare.model.Subtitle;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface SubtitleMapper {
    SubtitleDto toDto(Subtitle subtitle);

    Subtitle toModel(CreateSubtitleRequestDto requestDto);
}
