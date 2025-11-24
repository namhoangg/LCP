package com.lcp.acl.entity;

import com.lcp.acl.constants.enums.RoleTypeConstant;
import com.lcp.common.EntityBase;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "role")
@RequiredArgsConstructor
public class RoleEntity extends EntityBase {
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;

    private String name;
    private String description;

    @Enumerated(EnumType.STRING)
    private RoleTypeConstant type;

    @OneToMany(mappedBy = "role")
    private List<StaffRoleEntity> staffRoles = new ArrayList<>();

    @OneToMany(mappedBy = "role")
    private List<GroupRoleEntity> groupRoles = new ArrayList<>();
}
