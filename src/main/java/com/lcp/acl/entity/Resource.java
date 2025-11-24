package com.lcp.acl.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "resource")
public class Resource {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "resource_id_gen")
    @SequenceGenerator(name = "resource_id_gen", sequenceName = "permission_role_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Size(max = 255)
    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "parent_id")
    private Resource parent;

    @Column(name = "parent_id", insertable = false, updatable = false)
    private UUID parentId;

    // Add OneToMany mapping for children
    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    private List<Resource> children;
}