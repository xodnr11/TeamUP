package com.example.TeamUP.Repository;

import com.example.TeamUP.Entity.Team;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Long> {
    Optional<Team> findById(Long teamId);

    Page<Team> findAll(Pageable pageable);
}
