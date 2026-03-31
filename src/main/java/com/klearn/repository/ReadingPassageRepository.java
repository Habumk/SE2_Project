package com.klearn.repository;

import com.klearn.model.ReadingPassage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReadingPassageRepository extends JpaRepository<ReadingPassage, Long> {
}
