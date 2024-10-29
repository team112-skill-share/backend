package mate.academy.skillshare.service.internal;

import mate.academy.skillshare.dto.subtitle.CreateSubtitleRequestDto;
import mate.academy.skillshare.dto.subtitle.SubtitleDto;
import mate.academy.skillshare.model.Article;
import mate.academy.skillshare.model.Subtitle;

public interface SubtitleService {
    Subtitle createForArticle(Article article, CreateSubtitleRequestDto requestDto);

    SubtitleDto update(Long id, CreateSubtitleRequestDto requestDto);

    void delete(Long id);
}
