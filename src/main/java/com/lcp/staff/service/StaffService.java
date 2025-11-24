package com.lcp.staff.service;

import com.lcp.common.PageResponse;
import com.lcp.staff.dto.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StaffService {
    StaffResponseDto detail(Long id);

    List<StaffResponseDto> list();

//    StaffResponseDto signUp(StaffSignUpDto staffSignUpDto);

    StaffSignInResponseDto signIn(StaffSignInDto staffSignInDto);

    StaffSignOutResponseDto signOut();

    void forgotPassword(StaffForgotPasswordDto staffForgotPasswordDto);

    void changePassword(StaffChangePasswordDto staffChangePasswordDto);

    void create(CreateStaffDto createStaffDto);

    void update(UpdateStaffDto updateStaffDto);

    PageResponse<StaffResponseDto> listStaff(ListUserRequestDto listUserRequestDto);

    void delete(Long id);

    StaffResponseDto me();

    EvaluateUserResponse evaluateCurrentUser();

    StaffResponseDto uploadAvatar(MultipartFile file);

    void resetPassword(StaffResetPasswordDto staffResetPasswordDto);
}
