package mate.academy.skillshare.service.internal.impl;

import lombok.RequiredArgsConstructor;
import mate.academy.skillshare.dto.image.CreateImageRequestDto;
import mate.academy.skillshare.dto.image.ImageDto;
import mate.academy.skillshare.exception.EntityNotFoundException;
import mate.academy.skillshare.mapper.ImageMapper;
import mate.academy.skillshare.model.Article;
import mate.academy.skillshare.model.Course;
import mate.academy.skillshare.model.Image;
import mate.academy.skillshare.repository.image.ImageRepository;
import mate.academy.skillshare.service.internal.ImageService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
    private final ImageRepository imageRepository;
    private final ImageMapper imageMapper;

    @Override
    public Image createForCourse(Course course, CreateImageRequestDto requestDto) {
        Image image = imageMapper.toModel(requestDto);
        image.setCourse(course);
        return imageRepository.save(image);
    }

    @Override
    public Image createForArticle(Article article, CreateImageRequestDto requestDto) {
        Image image = imageMapper.toModel(requestDto);
        image.setArticle(article);
        return imageRepository.save(image);
    }

    @Override
    public ImageDto update(Long id, CreateImageRequestDto requestDto) {
        Image image = imageRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Can't find image by id: " + id));
        image.setUrl(requestDto.url());
        return imageMapper.toDto(imageRepository.save(image));
    }

    @Override
    public void delete(Long id) {
        imageRepository.deleteById(id);
    }
}
