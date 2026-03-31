package com.klearn.repository;

import com.klearn.model.UserProgress;
import com.klearn.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface UserProgressRepository extends JpaRepository<UserProgress, Long> {
    Optional<UserProgress> findByUser_UserIdAndLesson_LessonId(Long userId, Long lessonId);
    List<UserProgress> findByUser_UserId(Long userId);
    Optional<UserProgress> findByUser(User user);
}
