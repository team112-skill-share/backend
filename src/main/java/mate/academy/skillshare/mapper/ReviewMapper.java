package mate.academy.skillshare.mapper;

import java.util.List;
import mate.academy.skillshare.config.MapperConfig;
import mate.academy.skillshare.dto.review.CreateReviewRequestDto;
import mate.academy.skillshare.dto.review.ReviewDto;
import mate.academy.skillshare.model.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface ReviewMapper {
    @Mapping(target = "userId", source = "user.id")
    ReviewDto toDto(Review review);

    Review toModel(CreateReviewRequestDto requestDto);

    @Mapping(target = "userId", source = "user.id")
    List<ReviewDto> toDtoList(List<Review> reviews);
}
