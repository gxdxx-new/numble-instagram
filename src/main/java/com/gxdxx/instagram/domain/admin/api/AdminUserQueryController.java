package com.gxdxx.instagram.domain.admin.api;

import com.gxdxx.instagram.domain.admin.application.AdminUserQueryService;
import com.gxdxx.instagram.domain.admin.dto.request.AdminUserQueryRequest;
import com.gxdxx.instagram.domain.admin.dto.response.AdminUserQueryResponse;
import com.gxdxx.instagram.domain.post.dto.response.FeedResponse;
import com.gxdxx.instagram.global.common.dto.response.ErrorResponse;
import com.gxdxx.instagram.global.error.InvalidRequestException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "admin", description = "어드민 API")
@RequiredArgsConstructor
@RequestMapping("/admin")
@RestController
public class AdminUserQueryController {

    private final AdminUserQueryService adminUserQueryService;

    @PreAuthorize("@permissionService.hasPermission('READ_USER')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "유저 리스트 조회 성공",
                    content = @Content(schema = @Schema(implementation = AdminUserQueryResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Operation(summary = "어드민 유저 리스트 조회 메소드", description = "어드민 유저 리스트 조회 메소드입니다.")
    @GetMapping(value = "/users", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<AdminUserQueryResponse> findUsers(
            @PageableDefault Pageable pageable,
            @RequestBody @Valid AdminUserQueryRequest request,
            BindingResult bindingResult
    ) {
        validateRequest(bindingResult);
        return adminUserQueryService.findUsers(request.roleName(), pageable.getPageNumber(), pageable.getPageSize());
    }

    private void validateRequest(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new InvalidRequestException();
        }
    }

}
