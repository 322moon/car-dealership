package org.example.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "test_drives")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TestDriveJpaEntity extends BaseJpaEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private UserJpaEntity customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_id", nullable = false)
    private CarJpaEntity car;

    @Column(name = "test_date", nullable = false)
    private Instant testDate;

    @Column(name = "confirmed", nullable = false)
    private boolean isConfirm;
}
