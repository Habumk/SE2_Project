package com.klearn.repository;

import com.klearn.model.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
    @Query(value = "SELECT * FROM exercise WHERE type = 'listening' ORDER BY RAND() LIMIT :limit", nativeQuery = true)
    List<Exercise> findRandomListening(@Param("limit") int limit);

    List<Exercise> findByLesson_LessonId(Long lessonId);
    List<Exercise> findByLesson_LessonIdAndType(Long lessonId, Exercise.ExerciseType type);
}
