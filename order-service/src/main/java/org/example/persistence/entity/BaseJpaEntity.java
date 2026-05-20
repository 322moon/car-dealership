package org.example.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@MappedSuperclass
@Getter
public abstract class BaseJpaEntity {
    @Id
    @Column(nullable = false, updatable = false)
    @Setter
    private UUID id;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Setter
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    @Setter
    private Instant updatedAt;

    @Column(nullable = false)
    @Setter
    private boolean removed;

    @PrePersist
    protected void prePersist(){
        id = id == null ? UUID.randomUUID() : id;
        createdAt = Instant.now();
        updatedAt = createdAt;
        removed = false;
    }

    @PreUpdate
    protected void preUpdated(){
        updatedAt = Instant.now();
    }
}
