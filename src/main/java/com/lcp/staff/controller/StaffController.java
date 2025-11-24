package com.lcp.staff.controller;


import com.lcp.common.Constant;
import com.lcp.common.EmptyResponse;
import com.lcp.common.PageResponse;
import com.lcp.common.Response;
import com.lcp.staff.dto.*;
import com.lcp.staff.service.StaffService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/staff")
@RequiredArgsConstructor
public class StaffController {
    private final StaffService staffService;

//    @PostMapping(value = "/sign-up", consumes = Constant.API_CONTENT_TYPE)
//    public Response<StaffResponseDto> signUp(@Validated @RequestBody StaffSignUpDto staffSignUpDto) {
//        return Response.success(staffService.signUp(staffSignUpDto), ApiStaffMessage.STAFF_CREATE_SUCCESS);
//    }

    @PostMapping(value = "/create", consumes = Constant.API_CONTENT_TYPE)
    public Response<EmptyResponse> create(@Valid @RequestBody CreateStaffDto createStaffDto) {
        staffService.create(createStaffDto);
        return Response.success();
    }

    @PostMapping(value = "/update", consumes = Constant.API_CONTENT_TYPE)
    public Response<EmptyResponse> update(@Valid @RequestBody UpdateStaffDto updateStaffDto) {
        staffService.update(updateStaffDto);
        return Response.success();
    }

    @CrossOrigin(origins = "*")
    @PostMapping(value = "/sign-in", consumes = Constant.API_CONTENT_TYPE)
    public Response<StaffSignInResponseDto> signIn(@RequestBody StaffSignInDto staffSignInDto) {
        return Response.success(staffService.signIn(staffSignInDto));
    }

    @PostMapping(value = "/sign-out")
    public Response<StaffSignOutResponseDto> signOut() {
        return Response.success(staffService.signOut());
    }

    @GetMapping("/list")
    public Response<List<StaffResponseDto>> list() {
        return Response.success(staffService.list());
    }

    @GetMapping("/list-staff")
    public Response<PageResponse<StaffResponseDto>> listStaff(ListUserRequestDto listUserRequestDto) {
        return Response.success(staffService.listStaff(listUserRequestDto));
    }

    @GetMapping("")
    public Response<String> hello() {
        return Response.success("Hello World");
    }

    @PostMapping("/forgot-password")
    public Response<EmptyResponse> forgotPassword(@RequestBody StaffForgotPasswordDto staffForgotPasswordDto) {
        staffService.forgotPassword(staffForgotPasswordDto);
        return Response.success();
    }

    @PostMapping("/change-password")
    public Response<EmptyResponse> changePassword(@RequestBody StaffChangePasswordDto staffChangePasswordDto) {
        staffService.changePassword(staffChangePasswordDto);
        return Response.success();
    }

    @DeleteMapping("/delete/{id}")
    public Response<EmptyResponse> delete(@PathVariable Long id) {
        staffService.delete(id);
        return Response.success();
    }

    @GetMapping("/me")
    public StaffResponseDto me() {
        return staffService.me();
    }

    @GetMapping("/evaluation")
    public Response<EvaluateUserResponse> evaluation() {
        return Response.success(staffService.evaluateCurrentUser());
    }

    @PostMapping("/avatar/upload")
    public Response<StaffResponseDto> uploadAvatar(@RequestParam("file") MultipartFile file) {
        return Response.success(staffService.uploadAvatar(file));
    }

    @PostMapping("/reset-password")
    public Response<EmptyResponse> resetPassword(@Valid @RequestBody StaffResetPasswordDto staffResetPasswordDto) {
        staffService.resetPassword(staffResetPasswordDto);
        return Response.success();
    }
}
