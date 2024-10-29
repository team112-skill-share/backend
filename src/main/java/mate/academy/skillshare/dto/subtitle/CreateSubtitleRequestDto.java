package mate.academy.skillshare.dto.subtitle;

import jakarta.validation.constraints.NotBlank;

public record CreateSubtitleRequestDto(
        @NotBlank String subtitle
) {
}
