package mate.academy.skillshare.dto.content;

import jakarta.validation.constraints.NotBlank;

public record CreateContentRequestDto(
        String name,
        @NotBlank String text
) {
}
