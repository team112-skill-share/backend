package mate.academy.skillshare.util;

import mate.academy.skillshare.dto.image.CreateImageRequestDto;
import mate.academy.skillshare.dto.image.ImageDto;
import mate.academy.skillshare.model.Image;

public class ImageTestUtil {
    public static Image createTestImage() {
        Image image = new Image();
        image.setId(1L);
        image.setUrl("url");
        return image;
    }

    public static ImageDto createTestImageDto(Image image) {
        return new ImageDto(
                image.getId(),
                image.getUrl()
        );
    }

    public static CreateImageRequestDto createTestCreateImageRequestDto(Image image) {
        return new CreateImageRequestDto(
                image.getUrl()
        );
    }
}
