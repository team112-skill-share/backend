package mate.academy.skillshare.mapper;

import mate.academy.skillshare.config.MapperConfig;
import mate.academy.skillshare.dto.image.ImageDto;
import mate.academy.skillshare.model.Image;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface ImageMapper {
    ImageDto toDto(Image image);
}
