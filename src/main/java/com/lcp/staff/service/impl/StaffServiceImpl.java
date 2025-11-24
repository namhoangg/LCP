package com.lcp.staff.service.impl;

import com.lcp.acl.helper.AclHelper;
import com.lcp.client.entity.Client;
import com.lcp.client.mapper.ClientMapper;
import com.lcp.client.repository.ClientRepository;
import com.lcp.common.ApiMessageBase;
import com.lcp.common.PageResponse;
import com.lcp.common.dto.EmailBaseDto;
import com.lcp.common.impl.CacheService;
import com.lcp.exception.ApiException;
import com.lcp.minio.service.MinioService;
import com.lcp.security.AuthorizationService;
import com.lcp.security.JwtTokenProvider;
import com.lcp.security.UserDetailsCustom;
import com.lcp.security.configuration.AppProperties;
import com.lcp.staff.dto.*;
import com.lcp.staff.entity.Staff;
import com.lcp.staff.helper.StaffHelper;
import com.lcp.staff.mapper.StaffMapper;
import com.lcp.staff.repository.StaffCustomRepository;
import com.lcp.staff.repository.StaffRepository;
import com.lcp.staff.service.StaffService;
import com.lcp.util.AclUtil;
import com.lcp.util.EmailService;
import com.lcp.util.PersistentUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.StringSubstitutor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class StaffServiceImpl implements StaffService {

    private final StaffRepository staffRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthorizationService authorizationService;
    private final CacheService cacheService;
    private final AppProperties appProperties;
    private final EmailService emailService;
    private final StaffHelper staffHelper;
    private final StaffCustomRepository staffCustomRepository;
    private final AclHelper aclHelper;
    private final AclUtil aclUtil;
    private final MinioService minioService;
    private final ClientRepository clientRepository;

    @Override
    public StaffResponseDto detail(Long id) {
        Optional<Staff> staffOpt = staffRepository.findById(id);
        if (staffOpt.isEmpty()) {
            throw new ApiException(new ApiMessageBase("Staff not found"));
        }
        return StaffMapper.createResponse(staffOpt.get());
    }

    @Override
    public List<StaffResponseDto> list() {
        List<Staff> staffList = staffRepository.findAllByIsClientIsFalseAndIsSuperAdminFalse();
        List<StaffResponseDto> staffResponseDtoList = new ArrayList<>();
        for (Staff staff : staffList) {
            staffResponseDtoList.add(StaffMapper.createResponse(staff));
        }
        return staffResponseDtoList;
    }

//    @Override
//    @Transactional
//    public StaffResponseDto signUp(StaffSignUpDto staffSignUpDto) {
//        if (staffRepository.existsByEmail(staffSignUpDto.getEmail())) {
//            throw new ApiException(new ApiMessageBase("Email already exists"));
//        }
//        staffSignUpDto.setPassword(passwordEncoder.encode(staffSignUpDto.getPassword()));
//        Staff staff = StaffMapper.createEntity(staffSignUpDto);
//        staffRepository.save(staff);
//        PersistentUtil.flushAndClear();
//        return detail(staff.getId());
//    }

    @Override
    @Transactional
    public StaffSignInResponseDto signIn(StaffSignInDto staffSignInDto) {
        Optional<Staff> staffOpt = staffRepository.findByEmail(staffSignInDto.getEmail());
        if (staffOpt.isEmpty()) {
            throw new ApiException(new ApiMessageBase(HttpStatus.UNAUTHORIZED.value(), "Email not found"));
        }

        if (staffOpt.get().getForceChangePassword()) {
            StaffSignInResponseDto staffSignInResponseDto = new StaffSignInResponseDto();
            staffSignInResponseDto.setForceChangePassword(true);
            return staffSignInResponseDto;
        }

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            staffSignInDto.getEmail(),
                            staffSignInDto.getPassword()
                    )
            );
            String token = jwtTokenProvider.generateToken(
                    ((UserDetailsCustom) authentication.getPrincipal()).getUserId()
            );
            Staff staff = staffOpt.get();
            authorizationService.addToken(staff.getId(), token);
            staff.setLastLoginTime(LocalDateTime.now());
            staffRepository.saveAndFlush(staff);
            StaffSignInResponseDto staffSignInResponseDto = new StaffSignInResponseDto();
            staffSignInResponseDto.setToken(token);
            staffSignInResponseDto.setAccount(StaffMapper.createResponse(staff));
            return staffSignInResponseDto;

        } catch (Exception e) {
            throw new ApiException(new ApiMessageBase(HttpStatus.UNAUTHORIZED.value(), "Invalid password"));
        }
    }

    @Override
    public StaffSignOutResponseDto signOut() {
        authorizationService.removeTokenSession();
        return new StaffSignOutResponseDto();
    }

    @Override
    public void forgotPassword(StaffForgotPasswordDto staffForgotPasswordDto) {
        String email = staffForgotPasswordDto.getEmail();

        // Find the staff by email
        Staff staff = staffRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException("Staff not found with email: " + email));

        // Generate a reset token
        String token = generateResetToken();

        // Create key for the token
        String key = "password_reset:" + token;

        // Set token expiration time (30 mins)
        Date expiryDate = new Date(System.currentTimeMillis() + (30 * 60 * 1000));

        // Store token in cache with expiration
        cacheService.add(key, expiryDate, staff.getId().toString());

        // Generate reset URL
        String resetUrl = appProperties.getFrontendUrl() + "/reset-password?token=" + token;

        try {
            // Load email template
            String emailTemplate = StreamUtils.copyToString(
                    new ClassPathResource("email/forgot-password.html").getInputStream(),
                    StandardCharsets.UTF_8);

            // Replace placeholders in template
            EmailBaseDto emailBaseDto = getEmailBaseDto(staff, resetUrl, emailTemplate);

            // Send email
            emailService.send(emailBaseDto);

            log.info("Password reset link sent to staff: {}", email);
        } catch (Exception e) {
            log.error("Failed to send password reset email: {}", e.getMessage());
            throw new ApiException("Failed to send password reset email");
        }
    }

    @Override
    @Transactional
    public void changePassword(StaffChangePasswordDto dto) {
        Staff staff = staffRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new ApiException(new ApiMessageBase(HttpStatus.UNAUTHORIZED.value(), "Email not found")));

        // Authenticate old password
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getOldPassword())
            );
        } catch (Exception ex) {
            throw new ApiException(new ApiMessageBase(HttpStatus.UNAUTHORIZED.value(), "Invalid password"));
        }

        // Update password
        staff.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        staff.setForceChangePassword(false);
        staffRepository.save(staff);

        PersistentUtil.flushAndClear();
        authorizationService.removeTokenSession();
    }

    @Override
    @Transactional
    public void create(CreateStaffDto createStaffDto) {
        if (staffRepository.existsByEmail(createStaffDto.getEmail())) {
            throw new ApiException("Email already exists");
        }

        Staff staff = new Staff();
        staff.setEmail(createStaffDto.getEmail());
        String defaultPassword = "123456";
        staff.setPassword(passwordEncoder.encode(defaultPassword));
        staff.setFirstname(createStaffDto.getFirstname());
        staff.setLastname(createStaffDto.getLastname());
        staff.setPhone(createStaffDto.getPhone());
        staff.setIsClient(false);
        // Set it to false temporarily, frontend not yet implemented, Todo: set forceChangePassword to true
        staff.setForceChangePassword(false);
        staff.setCode(staffHelper.genStaffCode());
        staffRepository.save(staff);
    }

    @Override
    @Transactional
    public void update(UpdateStaffDto updateStaffDto) {
        Optional<Staff> staffOpt = staffRepository.findById(updateStaffDto.getId());
        if (staffOpt.isEmpty()) {
            throw new ApiException(new ApiMessageBase("Staff not found"));
        }

        Staff staff = staffOpt.get();
        if (staffRepository.existsByEmailAndIdNot(updateStaffDto.getEmail(), updateStaffDto.getId())) {
            throw new ApiException("Email already exists");
        }

        staff.setEmail(updateStaffDto.getEmail());
        staff.setFirstname(updateStaffDto.getFirstname());
        staff.setLastname(updateStaffDto.getLastname());
        staff.setPhone(updateStaffDto.getPhone());

        staffRepository.save(staff);
    }

    @Override
    public PageResponse<StaffResponseDto> listStaff(ListUserRequestDto listUserRequestDto) {
        Page<Staff> staffs = staffCustomRepository.findAll(listUserRequestDto);
        return PageResponse.buildPageDtoResponse(staffs, StaffMapper::createResponse);
    }

    @Override
    public void delete(Long id) {
        if (!staffRepository.existsById(id)) {
            throw new ApiException(new ApiMessageBase("Staff not found"));
        }
        staffRepository.deleteById(id);
    }

    @Override
    public StaffResponseDto me() {
        Long id = UserDetailsCustom.getCurrentUserId();

        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new ApiException(new ApiMessageBase("Staff not found")));
        StaffResponseDto staffResponseDto = StaffMapper.createResponse(staff);

        if (staff.getIsClient()) {
            Client client = clientRepository.findByStaffId(id);
            if (client != null) {
                staffResponseDto.setClient(ClientMapper.createResponse(client, ClientMapper.DetailIncludeFields));
            }
        }

        return staffResponseDto;
    }

    @Override
    public EvaluateUserResponse evaluateCurrentUser() {
        List<SubjectEvaluationDto> subjectEvaluationDtos = aclUtil.getAcls();

        EvaluateUserResponse responseDto = new EvaluateUserResponse();
        responseDto.setEvaluations(subjectEvaluationDtos);
        return responseDto;
    }

    @Override
    @Transactional
    public StaffResponseDto uploadAvatar(MultipartFile file) {
        Staff staff = staffRepository.findById(UserDetailsCustom.getCurrentUserId())
                .orElseThrow(() -> new ApiException(new ApiMessageBase("Staff not found")));

        String bucketAvatar = "avatar";
        String fileName = minioService.uploadFile(file, bucketAvatar);
        staff.setAvatar(fileName);
        staffRepository.save(staff);
        PersistentUtil.flushAndClear();
        return StaffMapper.createResponse(staff);
    }

    @Override
    @Transactional
    public void resetPassword(StaffResetPasswordDto staffResetPasswordDto) {
        if (!staffResetPasswordDto.getNewPassword().equals(staffResetPasswordDto.getConfirmPassword())) {
            throw new ApiException(new ApiMessageBase("Passwords do not match"));
        }

        // Authenticate the user with the token
        Staff staff = authenticateWithToken(staffResetPasswordDto.getToken());

        // Update the password
        staff.setPassword(passwordEncoder.encode(staffResetPasswordDto.getNewPassword()));
        staff.setForceChangePassword(false);
        staffRepository.save(staff);
    }

    private EmailBaseDto getEmailBaseDto(Staff staff, String resetUrl, String emailTemplate) {
        String displayName = staff.getLastname() != null ? staff.getLastname() : "";
        displayName += staff.getFirstname() != null ? " " + staff.getFirstname() : "";
        Map<String, String> valuesMap = Map.of(
                "displayName", staff.getLastname() + " " + staff.getFirstname(),
                "email", staff.getEmail(),
                "siteURL", appProperties.getFrontendUrl(),
                "resetPasswordURL", resetUrl);

        StringSubstitutor sub = new StringSubstitutor(valuesMap);
        String emailHtml = sub.replace(emailTemplate);

        // Create email DTO
        EmailBaseDto emailBaseDto = new EmailBaseDto();
        emailBaseDto.setSubject("FreightFlex Password Recovery");
        emailBaseDto.setBody(emailHtml);
        emailBaseDto.setToEmails(List.of(staff.getEmail()));
        return emailBaseDto;
    }

    /**
     * Generates a random token for password reset
     *
     * @return A random token string
     */
    private String generateResetToken() {
        return UUID.randomUUID().toString();
    }

    /**
     * Validates a password reset token
     *
     * @param token The token to validate
     * @return true if the token is valid, false otherwise
     */
    public boolean validateResetToken(String token) {
        String key = "password_reset:" + token;
        Set<Object> values = cacheService.retrieveAll(key);

        if (values == null || values.isEmpty()) {
            // Token doesn't exist or has expired
            return false;
        }

        // If found, delete the token (one-time use)
        cacheService.remove(key);

        // Return true indicating token was valid
        return true;
    }

    public Staff authenticateWithToken(String token) {
        String key = "password_reset:" + token;
        Set<Object> values = cacheService.retrieveAll(key);

        if (values == null || values.isEmpty()) {
            throw new ApiException("Invalid or expired password reset token");
        }

        // Get the first element from the set
        String staffId = values.iterator().next().toString();

        // Find the staff by ID
        Staff staff = staffRepository.findById(Long.valueOf(staffId))
                .orElseThrow(() -> new ApiException("Staff not found"));

        // Delete the token (one-time use)
        cacheService.remove(key);

        return staff;
    }
}
