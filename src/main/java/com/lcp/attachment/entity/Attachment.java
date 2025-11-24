package com.lcp.attachment.entity;

import com.lcp.common.EntityBase;
import com.lcp.common.enumeration.ResourceType;
import com.lcp.common.enumeration.SourceParentType;
import lombok.*;

import javax.persistence.*;

@Entity
@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "attachment")
public class Attachment extends EntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "attachment_gen")
    @SequenceGenerator(name = "attachment_gen", sequenceName = "attachment_seq", allocationSize = 1)
    @Column(name = "id", columnDefinition = "bigint default nextval('attachment_seq')")
    private Long id;
    private Long sourceId;
    @Enumerated(EnumType.STRING)
    private ResourceType resourceType;
    @Enumerated(EnumType.STRING)
    private SourceParentType sourceType;
    private String url;
    private String fileName;
    private String extension;
    private String description;
}
