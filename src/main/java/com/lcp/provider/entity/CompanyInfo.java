package com.lcp.provider.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "company_info")
public class CompanyInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "company_info_gen")
    @SequenceGenerator(name = "company_info_gen", sequenceName = "company_info_seq", allocationSize = 1)
    @Column(name = "id", columnDefinition = "bigint default nextval('company_info_seq')")
    private Long id;
    private String name;
    private String address;
    private String email;
    private String phone;
    private String city;
    private String country;
    private String zipCode;
    private String taxNumber;
    private boolean isDomestic;
}
