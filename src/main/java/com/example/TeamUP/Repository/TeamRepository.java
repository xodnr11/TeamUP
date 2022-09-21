package com.example.TeamUP.Repository;

import com.example.TeamUP.Entity.Team;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Long> {
    @EntityGraph(attributePaths = {"userInfo"}, type = EntityGraph.EntityGraphType.LOAD)
//    @Query(value = "select t from Team t join fetch t.userInfo")
    Optional<Team> findById(Long teamId);

    @EntityGraph(attributePaths = {"userInfo"}, type = EntityGraph.EntityGraphType.LOAD)
//    @Query(value = "select distinct t from Team t join fetch t.userInfo", nativeQuery = true)
    Page<Team> findAll(Pageable pageable);
}
