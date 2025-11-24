package com.lcp.provider.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "contact_person_info")
public class ContactPersonInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "contact_person_info_gen")
    @SequenceGenerator(name = "contact_person_info_gen", sequenceName = "contact_person_info_seq", allocationSize = 1)
    @Column(name = "id", columnDefinition = "bigint default nextval('contact_person_info_seq')")
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String position;
}
