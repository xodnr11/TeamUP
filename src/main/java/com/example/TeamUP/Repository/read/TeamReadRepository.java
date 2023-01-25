package com.example.TeamUP.Repository.read;

import com.example.TeamUP.Entity.Team;
import com.example.TeamUP.Repository.TeamQueryDslRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeamReadRepository extends JpaRepository<Team, Long>, TeamQueryDslRepository {
    @EntityGraph(attributePaths = {"userInfo"}, type = EntityGraph.EntityGraphType.LOAD)
//    @Query(value = "select t from Team t join fetch t.userInfo")
    Optional<Team> findById(Long teamId);

    @EntityGraph(attributePaths = {"userInfo"}, type = EntityGraph.EntityGraphType.LOAD)
//    @Query(value = "select distinct t from Team t join fetch t.userInfo", nativeQuery = true)
    Page<Team> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"userInfo"},type = EntityGraph.EntityGraphType.LOAD)
    Page<Team> findByCategory(Pageable pageable, String category);

    Team findByTitleContains(String title);

}
