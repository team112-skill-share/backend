package mate.academy.skillshare.service.internal;

import mate.academy.skillshare.dto.image.CreateImageRequestDto;
import mate.academy.skillshare.dto.image.ImageDto;
import mate.academy.skillshare.model.Article;
import mate.academy.skillshare.model.Course;
import mate.academy.skillshare.model.Image;

public interface ImageService {
    Image createForCourse(Course course, CreateImageRequestDto requestDto);

    Image createForArticle(Article article, CreateImageRequestDto requestDto);

    ImageDto update(Long id, CreateImageRequestDto requestDto);

    void delete(Long id);
}
