package com.lcp.security;

import com.lcp.staff.entity.Staff;
import com.lcp.staff.repository.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceCustom implements UserDetailsService {
    @Autowired
    StaffRepository staffRepository;

    /**
     * @param email
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Optional<Staff> staffOpt = staffRepository.findByEmail(email);

        if (staffOpt.isEmpty()) {
            throw new UsernameNotFoundException("Staff not found with email : " + email);
        }
        Staff staff = staffOpt.get();
        return UserDetailsCustom.builder()
                .userId(staff.getId())
                .username(staff.getEmail())
                .password(staff.getPassword())
                .build();
    }

    public UserDetails loadUserByUserClaims(UserClaims userClaims) {
        return new UserDetailsCustom(userClaims);
    }
}
