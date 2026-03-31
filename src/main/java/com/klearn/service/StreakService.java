package com.klearn.service;

import com.klearn.model.User;
import com.klearn.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class StreakService {

    private final UserRepository userRepository;
    private final BadgeService badgeService;

    @Transactional
    public void updateStreak(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) return;

        LocalDate today = LocalDate.now();
        LocalDate lastActive = user.getLastActiveDate();

        if (lastActive == null) {
            user.setStreakCount(1);
        } else {
            long daysBetween = ChronoUnit.DAYS.between(lastActive, today);
            if (daysBetween == 1) {
                user.setStreakCount(user.getStreakCount() + 1);
            } else if (daysBetween >= 2) {
                user.setStreakCount(1);
            }
            // daysBetween == 0 => keep streakCount as-is
            // daysBetween < 0 (future date) => keep streakCount as-is
        }
        user.setLastActiveDate(today);
        userRepository.save(user);

        // After updating streak, check and award streak-related badges
        badgeService.checkAndAwardBadges(userId);
    }
}
