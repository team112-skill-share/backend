package mate.academy.skillshare.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mate.academy.skillshare.dto.content.ContentDto;
import mate.academy.skillshare.dto.content.CreateContentRequestDto;
import mate.academy.skillshare.service.internal.ContentService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Content managing", description = "Endpoints for managing contents")
@RestController
@RequiredArgsConstructor
@RequestMapping("/contents")
public class ContentController {
    private final ContentService contentService;

    @Operation(summary = "Update a content", description = "Update a content by id")
    @PutMapping("/{id}")
    public ContentDto updateContent(@PathVariable Long id,
                                    @RequestBody @Valid CreateContentRequestDto requestDto) {
        return contentService.update(id, requestDto);
    }

    @Operation(summary = "Delete a content", description = "Delete a content by id")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteContent(@PathVariable Long id) {
        contentService.delete(id);
    }
}
