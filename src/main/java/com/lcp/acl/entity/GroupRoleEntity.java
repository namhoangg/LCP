package com.lcp.acl.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "staff_group_role")
@RequiredArgsConstructor
public class GroupRoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    private StaffGroupEntity group;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "role_id", nullable = false, referencedColumnName = "id", insertable = false, updatable = false)
    private RoleEntity role;

    @Column(name = "group_id", nullable = false)
    private UUID groupId;

    @Column(name = "role_id", nullable = false)
    private UUID roleId;
}
