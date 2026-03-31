package com.klearn.repository;

import com.klearn.model.GrammarLesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GrammarLessonRepository extends JpaRepository<GrammarLesson, Long> {
}
