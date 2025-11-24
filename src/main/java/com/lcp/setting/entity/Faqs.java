package com.lcp.setting.entity;

import com.lcp.common.EntityBase;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "faqs")
public class Faqs extends EntityBase {
    @Id
    @GeneratedValue
    private Long id;

    private String question;

    @Column(columnDefinition = "TEXT")
    private String answer;

    private Integer displayOrder;

    private Boolean isActive = true;

    // Optional: Category for organizing FAQs
    private String category;

    // Optional: For SEO purposes
    private String slug;
}