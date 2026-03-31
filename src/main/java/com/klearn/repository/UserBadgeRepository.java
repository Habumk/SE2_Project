package com.klearn.repository;

import com.klearn.model.UserBadge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserBadgeRepository extends JpaRepository<UserBadge, Long> {
    boolean existsByUser_UserIdAndBadge_BadgeId(Long userId, Long badgeId);
    List<UserBadge> findByUser_UserId(Long userId);
}
