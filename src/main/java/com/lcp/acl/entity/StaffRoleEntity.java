package com.lcp.acl.entity;

import com.lcp.staff.entity.Staff;
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
@Table(name = "staff_role")
@RequiredArgsConstructor
public class StaffRoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "staff_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    private Staff staff;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "role_id", nullable = false, referencedColumnName = "id", insertable = false, updatable = false)
    private RoleEntity role;

    @Column(name = "staff_id", nullable = false)
    private Long staffId;

    @Column(name = "role_id", nullable = false)
    private UUID roleId;
}
