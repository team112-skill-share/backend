package mate.academy.skillshare.service.external;

import java.io.IOException;
import java.nio.file.Path;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import mate.academy.skillshare.model.Article;
import mate.academy.skillshare.model.Course;
import mate.academy.skillshare.model.Image;
import mate.academy.skillshare.repository.image.ImageRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
@RequiredArgsConstructor
public class ImageService {
    private static final String BUCKET_NAME = "skillshare112-images";
    private final S3Client s3Client;
    private final ImageRepository imageRepository;

    public Image createForCourse(Course course, MultipartFile file) throws IOException {
        Image image = new Image();
        image.setUrl(uploadImage(file));
        image.setCourse(course);
        return imageRepository.save(image);
    }

    public Image createForArticle(Article article, MultipartFile file) throws IOException {
        Image image = new Image();
        image.setUrl(uploadImage(file));
        image.setArticle(article);
        return imageRepository.save(image);
    }

    public void delete(Long id) {
        deleteImage(id);
    }

    public String uploadImage(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path tempFile = Path.of(System.getProperty("java.io.tmpdir"), fileName);
        file.transferTo(tempFile);
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key(fileName)
                .acl("public-read")
                .build();
        s3Client.putObject(putObjectRequest, tempFile);
        return String.format("https://%s.s3.amazonaws.com/%s", BUCKET_NAME, fileName);
    }

    public void deleteImage(Long id) {
        Image image = imageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Image not found"));
        s3Client.deleteObject(DeleteObjectRequest.builder()
                .bucket("your-bucket-name")
                .key(image.getUrl())
                .build());
        imageRepository.deleteById(id);
    }
}
