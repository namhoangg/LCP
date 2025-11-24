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
@Table(name = "permission_resource_action")
public class PermissionResourceAction {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "permission_id", insertable = false, updatable = false)
    private Permission permission;

    @Column(name = "permission_id", nullable = false)
    private UUID permissionId;

    @Column(name = "resource_action_id", nullable = false)
    private Integer resourceActionId;

    @OneToOne
    @JoinColumn(name = "resource_action_id", referencedColumnName = "id", insertable = false, updatable = false)
    private ResourceAction resourceAction;
}