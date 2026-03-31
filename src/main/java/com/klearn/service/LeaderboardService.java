package com.klearn.service;

import com.klearn.dto.LeaderboardEntryDto;
import com.klearn.model.User;
import com.klearn.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * UC-12: Leaderboard – sorted by total_xp DESC.
 */
@Service
@RequiredArgsConstructor
public class LeaderboardService {

    private final UserRepository userRepository;

    public List<LeaderboardEntryDto> getTopLeaderboard(int limit) {
        List<User> users = userRepository.findAll();
        AtomicInteger rank = new AtomicInteger(1);

        return users.stream()
            .sorted(
                Comparator.comparing(User::getTotalXp, Comparator.nullsLast(Comparator.reverseOrder()))
                    .thenComparing(User::getCurrentLevel, Comparator.nullsLast(Comparator.reverseOrder()))
                    .thenComparing(User::getStreakCount, Comparator.nullsLast(Comparator.reverseOrder()))
            )
            .limit(limit)
            .map(u -> {
                LeaderboardEntryDto dto = new LeaderboardEntryDto();
                dto.setRank(rank.getAndIncrement());
                dto.setUserId(u.getUserId());
                dto.setName(u.getName());
                dto.setCurrentLevel(u.getCurrentLevel());
                dto.setTotalXp(u.getTotalXp());
                dto.setStreakCount(u.getStreakCount());
                return dto;
            })
            .collect(Collectors.toList());
    }
}
