package com.kanga.kickrushapi.api;

import com.kanga.kickrushapi.api.dto.AdminReleaseCreateRequest;
import com.kanga.kickrushapi.api.dto.AdminReleaseCreateResponse;
import com.kanga.kickrushapi.api.dto.AdminShoeCreateRequest;
import com.kanga.kickrushapi.api.dto.AdminShoeCreateResponse;
import com.kanga.kickrushapi.service.AdminService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/shoes")
    @ResponseStatus(HttpStatus.CREATED)
    public AdminShoeCreateResponse createShoe(@RequestBody AdminShoeCreateRequest request) {
        return adminService.createShoe(request);
    }

    @PostMapping("/releases")
    @ResponseStatus(HttpStatus.CREATED)
    public AdminReleaseCreateResponse createRelease(@RequestBody AdminReleaseCreateRequest request) {
        return adminService.createRelease(request);
    }
}
