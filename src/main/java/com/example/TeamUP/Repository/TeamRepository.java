package com.example.TeamUP.Repository;

import com.example.TeamUP.Entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {
}
