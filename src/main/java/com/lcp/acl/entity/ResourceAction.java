package com.lcp.acl.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "resource_action")
public class ResourceAction {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "resource_action_id_gen")
    @SequenceGenerator(name = "resource_action_id_gen", sequenceName = "resource_action_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "resource_id")
    private Resource resource;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "action_id")
    private Action action;

    @Size(max = 255)
    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "resource_id", insertable = false, updatable = false)
    private UUID resourceId;

    @Column(name = "action_id", insertable = false, updatable = false)
    private UUID actionId;
}