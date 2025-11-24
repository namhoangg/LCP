package com.lcp.staff.helper;

import com.lcp.staff.repository.StaffRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Component
public class StaffHelper {

    private static final String STAFF_CODE_FORMAT = "%s%06d";
    private static final String STAFF_CODE_PREFIX = "ST";
    private final StaffRepository staffRepository;

    public String genStaffCode() {
        long count = staffRepository.countDistinctByIsClientIsFalseAndIsSuperAdminFalse();
        return String.format(STAFF_CODE_FORMAT, STAFF_CODE_PREFIX, ++count);
    }
}
