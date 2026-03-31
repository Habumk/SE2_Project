package com.klearn.repository;

import com.klearn.model.VocabWord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface VocabWordRepository extends JpaRepository<VocabWord, Long> {
    List<VocabWord> findByCategoryOrderByIdAsc(String category);
}
