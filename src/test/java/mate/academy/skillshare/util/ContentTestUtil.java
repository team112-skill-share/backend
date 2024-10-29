package mate.academy.skillshare.util;

import mate.academy.skillshare.dto.content.ContentDto;
import mate.academy.skillshare.dto.content.CreateContentRequestDto;
import mate.academy.skillshare.model.Content;

public class ContentTestUtil {
    public static Content createTestContent() {
        Content content = new Content();
        content.setId(1L);
        content.setName("name");
        content.setText("text");
        return content;
    }

    public static ContentDto createTestContentDto(Content content) {
        return new ContentDto(
                content.getId(),
                content.getName(),
                content.getText()
        );
    }

    public static CreateContentRequestDto createTestCreateContentRequestDto(Content content) {
        return new CreateContentRequestDto(
                content.getName(),
                content.getText()
        );
    }
}
