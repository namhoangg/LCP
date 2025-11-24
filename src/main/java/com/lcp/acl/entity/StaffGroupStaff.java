package com.lcp.acl.entity;

import com.lcp.staff.entity.Staff;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "staff_group_staff", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"staff_group_id", "staff_id"}, name = "staff_group_staff_unique")
})
public class StaffGroupStaff {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "staff_group_id", nullable = false, insertable = false, updatable = false)
    private StaffGroupEntity staffGroup;

    @NotNull
    @Column(name = "staff_id", nullable = false)
    private Long staffId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "staff_id", nullable = false, insertable = false, updatable = false)
    private Staff staff;

    @NotNull
    @Column(name = "staff_group_id", nullable = false)
    private UUID staffGroupId;

    @NotNull
    @Column(name = "joined_date", nullable = false)
    private LocalDateTime joinedDate;

    @NotNull
    @Column(name = "added_by", nullable = false)
    private Long addedBy;

}