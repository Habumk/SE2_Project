package com.klearn.service;

import com.klearn.model.Badge;
import com.klearn.model.User;
import com.klearn.model.UserBadge;
import com.klearn.repository.BadgeRepository;
import com.klearn.repository.CourseRepository;
import com.klearn.repository.LessonRepository;
import com.klearn.repository.LessonResultRepository;
import com.klearn.repository.UserBadgeRepository;
import com.klearn.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BadgeService {

    private final UserRepository userRepository;
    private final BadgeRepository badgeRepository;
    private final UserBadgeRepository userBadgeRepository;
    private final LessonResultRepository lessonResultRepository;
    private final CourseRepository courseRepository;
    private final LessonRepository lessonRepository;

    @Transactional
    public void checkAndAwardBadges(Long userId) {
        checkAndAwardBadgesAndReturn(userId);
    }

    @Transactional
    public List<Badge> checkAndAwardBadgesAndReturn(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) return List.of();

        int streak = user.getStreakCount() != null ? user.getStreakCount() : 0;
        int totalXp = user.getTotalXp() != null ? user.getTotalXp() : 0;

        // Lesson completion derived from lesson_result rows
        List<com.klearn.model.LessonResult> lessonResults = lessonResultRepository.findByUser_UserId(userId);

        long lessonsCompletedCount = lessonResults.stream()
            .map(r -> r.getLesson() != null ? r.getLesson().getLessonId() : null)
            .distinct()
            .filter(x -> x != null)
            .count();

        long perfectSpeakingCount = lessonResults.stream()
            .filter(r -> r.getSpeakingScore() != null && r.getSpeakingScore() == 100)
            .count();

        // Course completion: all lessons in that course must be completed
        long coursesCompletedCount = 0;
        var allCourses = courseRepository.findAll();
        for (var course : allCourses) {
            if (course.getCourseId() == null) continue;

            int totalLessonsInCourse = lessonRepository.findByCourse_CourseIdOrderByOrderIndexAsc(course.getCourseId()).size();
            if (totalLessonsInCourse <= 0) continue;

            long completedLessonsInCourse = lessonResults.stream()
                .filter(r -> r.getLesson() != null && r.getLesson().getCourse() != null && r.getLesson().getCourse().getCourseId() != null)
                .filter(r -> course.getCourseId().equals(r.getLesson().getCourse().getCourseId()))
                .map(r -> r.getLesson().getLessonId())
                .distinct()
                .count();

            if (completedLessonsInCourse == totalLessonsInCourse) {
                coursesCompletedCount++;
            }
        }

        List<Badge> newlyEarned = new ArrayList<>();
        for (Badge badge : badgeRepository.findAll()) {
            String conditionType = badge.getConditionType();
            int conditionValue = badge.getConditionValue() != null ? badge.getConditionValue() : 0;

            boolean eligible = switch (conditionType) {
                case "lesson_complete" -> lessonsCompletedCount >= conditionValue;
                case "course_complete" -> coursesCompletedCount >= conditionValue;
                case "streak" -> streak >= conditionValue;
                case "xp_milestone" -> totalXp >= conditionValue;
                case "perfect_speaking" -> perfectSpeakingCount >= conditionValue;
                default -> false;
            };

            if (!eligible) continue;
            boolean exists = userBadgeRepository.existsByUser_UserIdAndBadge_BadgeId(user.getUserId(), badge.getBadgeId());
            if (!exists) {
                UserBadge userBadge = new UserBadge();
                userBadge.setUser(user);
                userBadge.setBadge(badge);
                userBadgeRepository.save(userBadge);
                newlyEarned.add(badge);
            }
        }

        return newlyEarned;
    }
}
