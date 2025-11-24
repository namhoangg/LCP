package com.lcp.client.dto;

import com.lcp.provider.dto.CompanyInfoResponseDto;
import com.lcp.provider.dto.ContactPersonInfoResponseDto;
import com.lcp.staff.dto.StaffResponseDto;
import lombok.Data;

@Data
public class ClientResponseDto {
    private Long id;
    private String name;
    private String email;
    private String code;
    private ContactPersonInfoResponseDto contactPersonInfo;
    private CompanyInfoResponseDto companyInfo;
    private Long staffId;
    private StaffResponseDto servedByStaff;
}
