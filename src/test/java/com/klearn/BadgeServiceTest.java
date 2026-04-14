package com.klearn.service;

import com.klearn.model.*;
import com.klearn.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for BadgeService
 */
@ExtendWith(MockitoExtension.class)
class BadgeServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BadgeRepository badgeRepository;

    @Mock
    private UserBadgeRepository userBadgeRepository;

    @Mock
    private LessonResultRepository lessonResultRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private LessonRepository lessonRepository;

    @InjectMocks
    private BadgeService badgeService;

    private User testUser;
    private Badge testBadge;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setUserId(1L);
        testUser.setStreakCount(0);
        testUser.setTotalXp(0);

        testBadge = new Badge();
        testBadge.setBadgeId(1L);
        testBadge.setName("First Step");
        testBadge.setConditionType("lesson_complete");
        testBadge.setConditionValue(1);
    }

    @Test
    void checkAndAwardBadges_ShouldAwardXpMilestoneBadge() {
        testUser.setTotalXp(100);
        Badge xpBadge = new Badge();
        xpBadge.setBadgeId(8L);
        xpBadge.setConditionType("xp_milestone");
        xpBadge.setConditionValue(100);

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(badgeRepository.findAll()).thenReturn(List.of(xpBadge));
        when(userBadgeRepository.existsByUser_UserIdAndBadge_BadgeId(1L, 8L)).thenReturn(false);
        when(lessonResultRepository.findByUser_UserId(1L)).thenReturn(List.of());
        when(courseRepository.findAll()).thenReturn(List.of());

        List<Badge> earned = badgeService.checkAndAwardBadgesAndReturn(1L);

        assertTrue(earned.contains(xpBadge));
        verify(userBadgeRepository, times(1)).save(any(UserBadge.class));
    }

    @Test
    void checkAndAwardBadges_ShouldNotAwardDuplicateBadge() {
        testUser.setStreakCount(3);
        Badge streakBadge = new Badge();
        streakBadge.setBadgeId(5L);
        streakBadge.setConditionType("streak");
        streakBadge.setConditionValue(3);

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(badgeRepository.findAll()).thenReturn(List.of(streakBadge));
        when(userBadgeRepository.existsByUser_UserIdAndBadge_BadgeId(1L, 5L)).thenReturn(true);
        when(lessonResultRepository.findByUser_UserId(1L)).thenReturn(List.of());
        when(courseRepository.findAll()).thenReturn(List.of());

        List<Badge> earned = badgeService.checkAndAwardBadgesAndReturn(1L);

        assertTrue(earned.isEmpty());
        verify(userBadgeRepository, never()).save(any(UserBadge.class));
    }

    @Test
    void getUserBadges_ShouldReturnUserBadges() {
        UserBadge userBadge = new UserBadge();
        userBadge.setBadge(testBadge);

        when(userBadgeRepository.findByUser_UserId(1L)).thenReturn(List.of(userBadge));

        List<Badge> badges = badgeService.getUserBadges(1L);

        assertEquals(1, badges.size());
        assertEquals("First Step", badges.get(0).getName());
    }
}
