package com.lcp.staff.entity;

import com.lcp.common.EntityBase;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "staff")
public class Staff extends EntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "staff_gen")
    @SequenceGenerator(name = "staff_gen", sequenceName = "staff_seq", allocationSize = 1)
    @Column(name = "id", columnDefinition = "bigint default nextval('staff_seq')")
    private Long id;
    private String code;
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private String phone;
    private LocalDateTime lastLoginTime;
    private String avatar;

    @Column(name = "is_client")
    private Boolean isClient;

    @Column(name = "force_change_password")
    private Boolean forceChangePassword;

    @Column(name = "is_super_admin", columnDefinition = "boolean default false")
    private Boolean isSuperAdmin = false;
}
