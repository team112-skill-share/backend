package mate.academy.skillshare.dto.content;

import jakarta.validation.constraints.NotBlank;

public record CreateContentRequestDto(
        @NotBlank String name,
        @NotBlank String text
) {
}
