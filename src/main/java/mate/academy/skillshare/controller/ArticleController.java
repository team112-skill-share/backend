package mate.academy.skillshare.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.skillshare.dto.article.ArticleDto;
import mate.academy.skillshare.dto.article.CreateArticleRequestDto;
import mate.academy.skillshare.service.internal.ArticleService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Article managing", description = "Endpoints for managing articles")
@RestController
@RequiredArgsConstructor
@RequestMapping("/articles")
public class ArticleController {
    private final ArticleService articleService;

    @Operation(summary = "Create an article", description = "Create a new article")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ArticleDto createArticle(@RequestBody @Valid CreateArticleRequestDto requestDto) {
        return articleService.create(requestDto);
    }

    @Operation(summary = "Retrieve articles", description = "Retrieve all articles")
    @GetMapping
    public List<ArticleDto> getAllArticles(@ParameterObject @PageableDefault Pageable pageable) {
        return articleService.getAll(pageable);
    }

    @Operation(summary = "Retrieve an article", description = "Retrieve an article by id")
    @GetMapping("/{id}")
    public ArticleDto getArticle(@PathVariable Long id) {
        return articleService.get(id);
    }

    @Operation(summary = "Update an article", description = "Update an article by id")
    @PutMapping("/{id}")
    public ArticleDto updateArticle(@PathVariable Long id,
                                    @RequestBody CreateArticleRequestDto requestDto) {
        return articleService.update(id, requestDto);
    }

    @Operation(summary = "Delete an article", description = "Delete an article by id")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteArticle(@PathVariable Long id) {
        articleService.delete(id);
    }
}
