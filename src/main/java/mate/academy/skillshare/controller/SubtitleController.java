package mate.academy.skillshare.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mate.academy.skillshare.dto.subtitle.CreateSubtitleRequestDto;
import mate.academy.skillshare.dto.subtitle.SubtitleDto;
import mate.academy.skillshare.service.internal.SubtitleService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Subtitle managing", description = "Endpoints for managing subtitles")
@RestController
@RequiredArgsConstructor
@RequestMapping("/subtitles")
public class SubtitleController {
    private final SubtitleService subtitleService;

    @Operation(summary = "Update a subtitle", description = "Update a subtitle by id")
    @PutMapping("/{id}")
    public SubtitleDto updateSubtitle(@PathVariable Long id,
                                      @RequestBody @Valid CreateSubtitleRequestDto requestDto) {
        return subtitleService.update(id, requestDto);
    }

    @Operation(summary = "Delete a subtitle", description = "Delete a subtitle by id")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteSubtitle(@PathVariable Long id) {
        subtitleService.delete(id);
    }
}
