package com.klearn.service;

import com.klearn.model.Course;
import com.klearn.model.Lesson;
import com.klearn.model.UserProgress;
import com.klearn.repository.CourseRepository;
import com.klearn.repository.LessonRepository;
import com.klearn.repository.UserProgressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * UC-04: Course & Lesson listing with per-user progress status.
 */
@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final LessonRepository lessonRepository;
    private final UserProgressRepository userProgressRepository;

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Course getCourseById(Long id) {
        return courseRepository.findById(id).orElse(null);
    }

    public List<Lesson> getLessonsByCourse(Long courseId) {
        return lessonRepository.findByCourse_CourseIdOrderByOrderIndexAsc(courseId);
    }

    /**
     * Returns a map of { lessonId → status } for a given user's progress.
     * Used by CourseController to pass per-lesson status to the template.
     */
    public Map<Long, String> getLessonStatusByUserId(Long userId) {
        List<UserProgress> progressList = userProgressRepository.findByUser_UserId(userId);
        Map<Long, String> statusByLessonId = new HashMap<>();
        for (UserProgress p : progressList) {
            if (p.getLesson() != null && p.getLesson().getLessonId() != null) {
                statusByLessonId.put(p.getLesson().getLessonId(), p.getStatus());
            }
        }
        return statusByLessonId;
    }
}
