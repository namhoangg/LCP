package com.lcp.acl.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "permission")
public class Permission {
    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Size(max = 255)
    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Size(max = 255)
    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @NotNull
    @Column(name = "scope", nullable = false)
    private Integer scope;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @NotNull
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @NotNull
    @Column(name = "created_by", nullable = false)
    private Long createdBy;

    @NotNull
    @Column(name = "updated_by", nullable = false)
    private Long updatedBy;

    // Map to role
    @OneToMany
    @JoinColumn(name = "permission_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    private List<PermissionRole> permissionRole;

    // Map to resource action
    @OneToMany
    @JoinColumn(name = "permission_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    private List<PermissionResourceAction> permissionResourceAction;
}