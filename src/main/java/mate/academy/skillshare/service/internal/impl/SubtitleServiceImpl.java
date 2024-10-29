package mate.academy.skillshare.service.internal.impl;

import lombok.RequiredArgsConstructor;
import mate.academy.skillshare.dto.subtitle.CreateSubtitleRequestDto;
import mate.academy.skillshare.dto.subtitle.SubtitleDto;
import mate.academy.skillshare.exception.EntityNotFoundException;
import mate.academy.skillshare.mapper.SubtitleMapper;
import mate.academy.skillshare.model.Article;
import mate.academy.skillshare.model.Subtitle;
import mate.academy.skillshare.repository.subtitle.SubtitleRepository;
import mate.academy.skillshare.service.internal.SubtitleService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubtitleServiceImpl implements SubtitleService {
    private final SubtitleRepository subtitleRepository;
    private final SubtitleMapper subtitleMapper;

    @Override
    public Subtitle createForArticle(Article article, CreateSubtitleRequestDto requestDto) {
        Subtitle subtitle = subtitleMapper.toModel(requestDto);
        subtitle.setArticle(article);
        return subtitleRepository.save(subtitle);
    }

    @Override
    public SubtitleDto update(Long id, CreateSubtitleRequestDto requestDto) {
        Subtitle subtitle = subtitleRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Can't find subtitle by id: " + id));
        subtitle.setSubtitle(requestDto.subtitle());
        return subtitleMapper.toDto(subtitleRepository.save(subtitle));
    }

    @Override
    public void delete(Long id) {
        subtitleRepository.deleteById(id);
    }
}
