package com.klearn.repository;

import com.klearn.model.Theory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TheoryRepository extends JpaRepository<Theory, Long> {
    Optional<Theory> findByLesson_LessonId(Long lessonId);
}
