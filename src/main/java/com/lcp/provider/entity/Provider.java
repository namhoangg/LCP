package com.lcp.provider.entity;

import com.lcp.common.EntityBase;
import com.lcp.provider.enumeration.ProviderType;
import com.lcp.rate.entity.FclRate;
import com.lcp.rate.entity.LclRate;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "provider")
public class Provider extends EntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "provider_gen")
    @SequenceGenerator(name = "provider_gen", sequenceName = "provider_seq", allocationSize = 1)
    @Column(name = "id", columnDefinition = "bigint default nextval('provider_seq')")
    private Long id;
    private String name;
    private String code;
    @Enumerated(EnumType.STRING)
    private ProviderType type;
    private String trackingUrl;
    private Long companyInfoId;
    private Long contactPersonInfoId;

    @OneToOne()
    @JoinColumn(name = "companyInfoId", referencedColumnName = "id", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "fk_provider_company_info"))
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private CompanyInfo companyInfo;

    @OneToOne()
    @JoinColumn(name = "contactPersonInfoId", referencedColumnName = "id", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "fk_provider_contact_person_info"))
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private ContactPersonInfo contactPersonInfo;

    // Foreign one to many to fcl rate and lcl rate
    @OneToMany
    @JoinColumn(name = "providerId", referencedColumnName = "id", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "fk_provider_fcl_rate"))
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<FclRate> fclRates;

    @OneToMany
    @JoinColumn(name = "providerId", referencedColumnName = "id", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "fk_provider_lcl_rate"))
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<LclRate> lclRates;
}
