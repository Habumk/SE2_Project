package com.klearn.service;

import com.klearn.model.User;
import com.klearn.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class XpService {

    private final UserRepository userRepository;

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
