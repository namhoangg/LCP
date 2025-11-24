package com.lcp.common.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "document")
public class Document {

    @Id
    private Long id;
    private String name;
    private String originalName;
    private String description;
    private LocalDateTime createdAt;
    private Long createdBy;
}
