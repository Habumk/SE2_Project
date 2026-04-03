package com.klearn.repository;

import com.klearn.model.Vocabulary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VocabularyRepository extends JpaRepository<Vocabulary, Long> {
    @Query(value = "SELECT * FROM vocabulary ORDER BY RAND() LIMIT :limit", nativeQuery = true)
    List<Vocabulary> findRandomVocabs(@Param("limit") int limit);
    List<Vocabulary> findByLesson_LessonId(Long lessonId);
}
