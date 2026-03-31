package com.klearn.repository;

import com.klearn.model.ListeningExercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ListeningExerciseRepository extends JpaRepository<ListeningExercise, Long> {
}
