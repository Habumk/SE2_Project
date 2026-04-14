package com.klearn.service;

import com.klearn.model.Badge;
import com.klearn.model.User;
import com.klearn.model.UserBadge;
import com.klearn.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for StreakService
 */
@ExtendWith(MockitoExtension.class)
class StreakServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BadgeService badgeService;

    @InjectMocks
    private StreakService streakService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setUserId(1L);
        testUser.setStreakCount(0);
        testUser.setLastActiveDate(null);
    }

    @Test
    void updateStreak_FirstDay_ShouldSetStreakTo1() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        streakService.updateStreak(1L);

        assertEquals(1, testUser.getStreakCount());
        assertEquals(LocalDate.now(), testUser.getLastActiveDate());
        verify(userRepository, times(1)).save(testUser);
    }

    @Test
    void updateStreak_ConsecutiveDay_ShouldIncrementStreak() {
        testUser.setStreakCount(5);
        testUser.setLastActiveDate(LocalDate.now().minusDays(1));
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        streakService.updateStreak(1L);

        assertEquals(6, testUser.getStreakCount());
        verify(userRepository, times(1)).save(testUser);
        verify(badgeService, times(1)).checkAndAwardBadges(1L);
    }

    @Test
    void updateStreak_SkippedDay_ShouldResetStreak() {
        testUser.setStreakCount(10);
        testUser.setLastActiveDate(LocalDate.now().minusDays(2));
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        streakService.updateStreak(1L);

        assertEquals(1, testUser.getStreakCount());
        verify(userRepository, times(1)).save(testUser);
    }

    @Test
    void updateStreak_SameDay_ShouldKeepStreak() {
        testUser.setStreakCount(5);
        testUser.setLastActiveDate(LocalDate.now());
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        streakService.updateStreak(1L);

        assertEquals(5, testUser.getStreakCount());
        verify(userRepository, times(1)).save(testUser);
    }

    @Test
    void updateStreak_UserNotFound_ShouldDoNothing() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        streakService.updateStreak(99L);

        verify(userRepository, never()).save(any());
    }
}
