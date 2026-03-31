package com.klearn.repository;

import com.klearn.model.RoomParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomParticipantRepository extends JpaRepository<RoomParticipant, Long> {
    List<RoomParticipant> findByRoom_RoomId(Long roomId);
    Optional<RoomParticipant> findByRoom_RoomIdAndUser_UserId(Long roomId, Long userId);
}
