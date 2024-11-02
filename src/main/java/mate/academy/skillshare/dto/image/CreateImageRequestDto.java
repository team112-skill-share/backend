package mate.academy.skillshare.dto.image;

import jakarta.validation.constraints.NotBlank;

public record CreateImageRequestDto(
        @NotBlank String url
) {
}
