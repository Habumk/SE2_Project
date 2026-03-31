package com.klearn.repository;

import com.klearn.model.HangulCharacter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface HangulCharacterRepository extends JpaRepository<HangulCharacter, Long> {
    List<HangulCharacter> findByTypeOrderByIdAsc(String type);
}
