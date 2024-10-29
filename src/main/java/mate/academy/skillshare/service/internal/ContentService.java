package mate.academy.skillshare.service.internal;

import mate.academy.skillshare.dto.content.ContentDto;
import mate.academy.skillshare.dto.content.CreateContentRequestDto;
import mate.academy.skillshare.model.Article;
import mate.academy.skillshare.model.Content;
import mate.academy.skillshare.model.Course;

public interface ContentService {
    Content createForCourse(Course course, CreateContentRequestDto requestDto);

    Content createForArticle(Article article, CreateContentRequestDto requestDto);

    ContentDto update(Long id, CreateContentRequestDto requestDto);

    void delete(Long id);
}
