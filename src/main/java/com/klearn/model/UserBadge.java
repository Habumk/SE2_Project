package com.klearn.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_badge", uniqueConstraints = {
    @UniqueConstraint(name = "uq_user_badge", columnNames = {"user_id", "badge_id"})
})
@Data
@NoArgsConstructor
public class UserBadge {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "badge_id", nullable = false)
    private Badge badge;

    @Column(name = "earned_at")
    private LocalDateTime earnedAt = LocalDateTime.now();
}
