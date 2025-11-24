package com.lcp.setting.entity;

import com.lcp.common.EntityBase;

import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.persistence.*;

@Entity
@Table(name = "unloco")
@EqualsAndHashCode(callSuper = true)
@Data
public class Unloco extends EntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "unloco_gen")
    @SequenceGenerator(name = "unloco_gen", sequenceName = "unloco_seq", allocationSize = 1)
    @Column(name = "id", columnDefinition = "bigint default nextval('unloco_seq')")
    private Long id;
    private String code;
    private String cityName;
    private String cityCode;
    private String countryCode;
    private String countryName;

    @PrePersist
    @PreUpdate
    private void updateCode() {
        this.code = this.countryCode + this.cityCode;
    }

    @Transient
    public String getDisplayName() {
        return String.format("%s - %s, %s", code, cityName, countryName);
    }
}
