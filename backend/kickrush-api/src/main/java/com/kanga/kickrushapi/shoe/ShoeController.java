package com.kanga.kickrushapi.shoe;

import com.kanga.kickrushapi.common.PageResponse;
import com.kanga.kickrushapi.common.PageUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.kanga.kickrushapi.shoe.dto.ShoeDetailDto;
import com.kanga.kickrushapi.shoe.dto.ShoeDto;

import java.util.List;

@RestController
@RequestMapping("/api/shoes")
public class ShoeController {

    private final ShoeQueryService shoeQueryService;

    public ShoeController(ShoeQueryService shoeQueryService) {
        this.shoeQueryService = shoeQueryService;
    }

    @GetMapping
    public PageResponse<ShoeDto> list(@RequestParam(required = false) String brand,
                                     @RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "20") int size) {
        List<ShoeDto> shoes = shoeQueryService.findAll(brand);
        return PageUtils.paginate(shoes, page, size);
    }

    @GetMapping("/{id}")
    public ShoeDetailDto detail(@PathVariable Long id) {
        return shoeQueryService.findDetail(id);
    }
}
