package com.lcp.acl.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "permission_role")
public class PermissionRole {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "permission_role_id_gen")
    @SequenceGenerator(name = "permission_role_id_gen", sequenceName = "permission_role_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "permission_id", insertable = false, updatable = false)
    private Permission permission;

    @Column(name = "permission_id", nullable = false)
    private UUID permissionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "role_id", insertable = false, updatable = false)
    private RoleEntity role;

    @Column(name = "role_id", nullable = false)
    private UUID roleId;
}