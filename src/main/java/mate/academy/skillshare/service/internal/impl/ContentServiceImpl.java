package mate.academy.skillshare.service.internal.impl;

import lombok.RequiredArgsConstructor;
import mate.academy.skillshare.dto.content.ContentDto;
import mate.academy.skillshare.dto.content.CreateContentRequestDto;
import mate.academy.skillshare.exception.EntityNotFoundException;
import mate.academy.skillshare.mapper.ContentMapper;
import mate.academy.skillshare.model.Article;
import mate.academy.skillshare.model.Content;
import mate.academy.skillshare.model.Course;
import mate.academy.skillshare.repository.content.ContentRepository;
import mate.academy.skillshare.service.internal.ContentService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContentServiceImpl implements ContentService {
    private final ContentRepository contentRepository;
    private final ContentMapper contentMapper;

    @Override
    public Content createForCourse(Course course, CreateContentRequestDto requestDto) {
        Content content = contentMapper.toModel(requestDto);
        content.setCourse(course);
        return contentRepository.save(content);
    }

    @Override
    public Content createForArticle(Article article, CreateContentRequestDto requestDto) {
        Content content = contentMapper.toModel(requestDto);
        content.setArticle(article);
        return contentRepository.save(content);
    }

    @Override
    public ContentDto update(Long id, CreateContentRequestDto requestDto) {
        Content content = contentRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Can't find content by id: " + id));
        content.setName(requestDto.name());
        content.setText(requestDto.text());
        return contentMapper.toDto(contentRepository.save(content));
    }

    @Override
    public void delete(Long id) {
        contentRepository.deleteById(id);
    }
}
