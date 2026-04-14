package com.klearn.service;

import com.klearn.model.LessonResult;
import com.klearn.model.User;
import com.klearn.repository.LessonResultRepository;
import com.klearn.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class XpService {

    private final UserRepository userRepository;
    private final LessonResultRepository lessonResultRepository;

    @Transactional
    public void addXp(Long userId, int xpReward) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null || xpReward <= 0) return;

        int newTotalXp = user.getTotalXp() + xpReward;
        user.setTotalXp(newTotalXp);
        
        int newLevel = calculateLevel(newTotalXp);
        if (newLevel > user.getCurrentLevel()) {
            user.setCurrentLevel(newLevel);
            // Fire level up event here if needed
        }
        
        userRepository.save(user);
    }

    @Transactional
    public void recalculateUserStats(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) return;

        // Recalculate total XP from completed lessons
        var lessonResults = lessonResultRepository.findByUser_UserId(userId);
        int totalXpFromLessons = lessonResults.stream()
            .mapToInt(LessonResult::getXpEarned)
            .sum();

        // Debug logging
        System.out.println("DEBUG: User " + userId + " has " + lessonResults.size() + " lesson results");
        System.out.println("DEBUG: Total XP from lessons: " + totalXpFromLessons);
        System.out.println("DEBUG: Previous total XP: " + user.getTotalXp());

        user.setTotalXp(totalXpFromLessons);

        // Recalculate level from total XP
        int newLevel = calculateLevel(totalXpFromLessons);
        System.out.println("DEBUG: New level: " + newLevel + " (was " + user.getCurrentLevel() + ")");

        user.setCurrentLevel(newLevel);

        userRepository.save(user);
        System.out.println("DEBUG: User stats recalculated successfully");
    }

    @Transactional
    public void addSampleDataForTesting(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) return;

        // Create actual lesson result records for proper recalculation
        createSampleLessonResults(userId);

        // Update streak
        user.setStreakCount(5);
        user.setLastActiveDate(java.time.LocalDate.now());
        userRepository.save(user);

        System.out.println("DEBUG: Sample data added for user " + userId);
    }

    @Transactional
    public void createSampleLessonResults(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) return;

        // For testing purposes, we'll directly set the XP and level
        // since creating full LessonResult entities requires lesson references

        // Reset user stats first
        user.setTotalXp(0);
        user.setCurrentLevel(1);

        // Add sample XP (3 lessons × 100 XP = 300 XP)
        addXp(userId, 300);

        System.out.println("DEBUG: Created sample XP data for user " + userId);
    }

    private int calculateLevel(int totalXp) {
        // Level thresholds (per spec):
        // Level 1: 0–99 XP
        // Level 2: 100–249 XP
        // Level 3: 250–499 XP
        // Level 4: 500–999 XP
        // Level 5+: each next level adds 500 XP
        if (totalXp < 100) {
            return 1;
        }
        if (totalXp < 250) {
            return 2;
        }
        if (totalXp < 500) {
            return 3;
        }
        if (totalXp < 1000) {
            return 4;
        }
        return 5 + (totalXp - 1000) / 500;
    }
}