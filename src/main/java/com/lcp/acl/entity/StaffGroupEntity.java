package com.lcp.acl.entity;

import com.lcp.common.EntityBase;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "staff_group")
@RequiredArgsConstructor
public class StaffGroupEntity extends EntityBase {
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

    @OneToMany(mappedBy = "group")
    private Set<GroupRoleEntity> staffGroupRoles = new LinkedHashSet<>();

    @OneToMany(mappedBy = "staffGroup")
    private Set<StaffGroupStaff> staffGroupStaffs = new LinkedHashSet<>();

}
