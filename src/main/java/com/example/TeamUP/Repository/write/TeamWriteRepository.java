package com.example.TeamUP.Repository.write;

import com.example.TeamUP.Entity.Team;
import com.example.TeamUP.Repository.TeamQueryDslRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamWriteRepository extends JpaRepository<Team, Long>, TeamQueryDslRepository {
}
