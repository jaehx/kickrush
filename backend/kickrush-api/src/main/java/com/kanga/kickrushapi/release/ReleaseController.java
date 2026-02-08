package com.kanga.kickrushapi.release;

import com.kanga.kickrushapi.common.PageResponse;
import com.kanga.kickrushapi.common.PageUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.kanga.kickrushapi.release.dto.ReleaseDetailDto;
import com.kanga.kickrushapi.release.dto.ReleaseDto;

import java.util.List;

@RestController
@RequestMapping("/api/releases")
public class ReleaseController {

    private final ReleaseQueryService releaseQueryService;

    public ReleaseController(ReleaseQueryService releaseQueryService) {
        this.releaseQueryService = releaseQueryService;
    }

    @GetMapping
    public PageResponse<ReleaseDto> list(@RequestParam(required = false) String status,
                                         @RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "20") int size) {
        List<ReleaseDto> releases = releaseQueryService.findAll(status);
        return PageUtils.paginate(releases, page, size);
    }

    @GetMapping("/{id}")
    public ReleaseDetailDto detail(@PathVariable Long id) {
        return releaseQueryService.findDetail(id);
    }
}
