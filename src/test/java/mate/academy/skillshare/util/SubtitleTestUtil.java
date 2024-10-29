package mate.academy.skillshare.util;

import mate.academy.skillshare.dto.subtitle.CreateSubtitleRequestDto;
import mate.academy.skillshare.dto.subtitle.SubtitleDto;
import mate.academy.skillshare.model.Subtitle;

public class SubtitleTestUtil {
    public static Subtitle createTestSubtitle() {
        Subtitle subtitle = new Subtitle();
        subtitle.setId(1L);
        subtitle.setSubtitle("subtitle");
        return subtitle;
    }

    public static SubtitleDto createTestSubtitleDto(Subtitle subtitle) {
        return new SubtitleDto(
                subtitle.getId(),
                subtitle.getSubtitle()
        );
    }

    public static CreateSubtitleRequestDto createTestCreateSubtitleRequestDto(Subtitle subtitle) {
        return new CreateSubtitleRequestDto(
                subtitle.getSubtitle()
        );
    }
}
