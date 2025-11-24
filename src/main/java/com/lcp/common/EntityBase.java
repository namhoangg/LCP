package com.lcp.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lcp.common.enumeration.GeneralStatus;
import com.lcp.security.UserDetailsCustom;
import com.lcp.staff.entity.Staff;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@NoArgsConstructor
@MappedSuperclass
@SuperBuilder
@Data
public abstract class EntityBase implements Serializable {

    private LocalDateTime createdAt;

    private Long createdBy;

    private LocalDateTime updatedAt;

    private Long updatedBy;

    @Enumerated(EnumType.STRING)
    private GeneralStatus status = GeneralStatus.ACTIVE;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "createdBy", referencedColumnName = "id", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "fk_created_by_staff"))
    @EqualsAndHashCode.Exclude
    private Staff createdUser;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updatedBy", referencedColumnName = "id", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "fk_updated_by_staff"))
    @EqualsAndHashCode.Exclude
    private Staff updatedUser;

    @PrePersist
    public void onPrePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
        UserDetailsCustom user = UserDetailsCustom.getCurrentUser();
        if (user != null) {
            if (this.createdBy == null) this.createdBy = user.getUserId();
            this.updatedBy = user.getUserId();
        }
    }

    @PreUpdate
    public void onPreUpdate() {
        this.updatedAt = LocalDateTime.now();
        UserDetailsCustom user = UserDetailsCustom.getCurrentUser();
        if (user != null) {
            this.updatedBy = user.getUserId();
        }
    }
}
