package com.example.TeamUP.Repository;

import com.example.TeamUP.Entity.Calendar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface CalendarRepository extends JpaRepository<Calendar, Long> {
    List<Calendar> findByTeam_id(Long teamId);

    Calendar findByTeamIdAndDate(Long teamId, Date date);
}
