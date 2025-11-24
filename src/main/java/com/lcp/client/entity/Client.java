package com.lcp.client.entity;

import com.lcp.common.EntityBase;
import com.lcp.provider.entity.CompanyInfo;
import com.lcp.provider.entity.ContactPersonInfo;
import com.lcp.staff.entity.Staff;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "client")
public class Client extends EntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "client_gen")
    @SequenceGenerator(name = "client_gen", sequenceName = "client_seq", allocationSize = 1)
    @Column(name = "id", columnDefinition = "bigint default nextval('client_seq')")
    private Long id;
    private String name;
    private String code;
    private String email;
    private Long companyInfoId;
    private Long contactPersonInfoId;
    private Long staffId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "companyInfoId", referencedColumnName = "id", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "fk_client_company_info"))
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private CompanyInfo companyInfo;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contactPersonInfoId", referencedColumnName = "id", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "fk_client_contact_person_info"))
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private ContactPersonInfo contactPersonInfo;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "staffId", referencedColumnName = "id", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "fk_client_staff"))
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Staff staff;

    @Column(name = "served_by")
    private Long servedBy;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "served_by", referencedColumnName = "id", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "fk_client_served_by"))
    private Staff servedByStaff;
}
