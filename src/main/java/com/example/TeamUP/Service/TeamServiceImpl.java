package com.example.TeamUP.Service;

import com.example.TeamUP.DTO.RequestCreateTeamDTO;
import com.example.TeamUP.DTO.ResponsePostDTO;
import com.example.TeamUP.DTO.ResponseTeamDTO;
import com.example.TeamUP.Entity.*;
import com.example.TeamUP.Entity.Calendar;
import com.example.TeamUP.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService{

    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;
    private final TeamRegisterRepository teamRegisterRepository;
    private final CalendarRepository calendarRepository;

    @Override
    public void joinTeam(Team team, UserInfo userInfo, Role role) {

        TeamMember teamMember = new TeamMember();

        teamMember.setTeam(team);
        teamMember.setUserInfo(userInfo);
        teamMember.setRole(role.getKey());

        teamMemberRepository.save(teamMember);
    }

    @Override
    public Team createTeam(RequestCreateTeamDTO team, UserInfo user) {

        Team teamEntity = new Team();

        teamEntity.setTeamName(team.getTeamName());
        teamEntity.setTitle(team.getTitle());
        teamEntity.setUserInfo(user);
        teamEntity.setTeamPresent(team.getPresent());
        teamEntity.setCategory(team.getCategory());
        teamEntity.setContent(team.getContent());
        teamEntity.setMaxMember(team.getMax_member());
        teamEntity.setCurrentMember(1);

        teamRepository.save(teamEntity);

        joinTeam(teamEntity, user, Role.MASTER);

        return teamEntity;
    }

    @Override
    public void insertTag(Team team, List<Map<String, String>> rawTags) {

        List<Tag> tags = new ArrayList<>();

        for (Map<String, String> tag :rawTags){
            Tag teamTag = Tag.builder()
                    .team(team)
                    .tagName(tag.get("tag"))
                    .build();

            tags.add(teamTag);
        }

        tagRepository.saveAll(tags);
    }

    @Override
    public ResponsePostDTO getPostInfo(Long id, Long teamId) {

        Optional<Team> team = teamRepository.findById(teamId);
        String writer = userRepository.findById(team.get().getUserInfo().getId()).get().getNickname();
        ResponsePostDTO responsePostDTO = new ResponsePostDTO();

        boolean registered = false;

        List<Map<String,Object>> memberList = getTeamMember(teamId);
        for (Map<String,Object> member : memberList){
            if (member.get("user_id").equals(id)) {
                registered = true;
                break;
            }
        }

        responsePostDTO.setTeamId(teamId);
        responsePostDTO.setTitle(team.get().getTitle());
        responsePostDTO.setContent(team.get().getContent());
        responsePostDTO.setWriter(writer);
        responsePostDTO.setMax_member(team.get().getMaxMember());
        responsePostDTO.setCurrent_member(team.get().getCurrentMember());
        responsePostDTO.setTeam_member(memberList);
        responsePostDTO.setRegistered(registered);

        return responsePostDTO;
    }

    @Override
    public ResponseTeamDTO getTeamInfo(Long userId, Long teamId) {

        ResponseTeamDTO responseTeamDTO = new ResponseTeamDTO();
        Optional<Team> rawTeam = teamRepository.findById(teamId);

        if (rawTeam.isPresent()){
            Team team = rawTeam.get();
            responseTeamDTO.setTeamId(team.getId());
            responseTeamDTO.setTitle(team.getTitle());
            responseTeamDTO.setContent(team.getContent());
            responseTeamDTO.setTeam_name(team.getTeamName());
            responseTeamDTO.setTeam_present(team.getTeamPresent());
            responseTeamDTO.setManager(team.getUserInfo().getId().equals(userId));
        }

        responseTeamDTO.setTeam_member(getTeamMember(teamId));
        responseTeamDTO.setRegisters(getTeamRegister(teamId));
        responseTeamDTO.setCalendar(getTeamCalendar(teamId));

        return responseTeamDTO;
    }

    @Override
    public List<Map<String,Object>> getTeamMember(Long teamId) {

        List<Map<String,Object>> memberList = new ArrayList<>();
        List<TeamMember> members = teamMemberRepository.findAllByTeam_Id(teamId);

        for (TeamMember m : members){

            Map<String,Object> member = new HashMap<>();

            member.put("user_id", m.getUserInfo().getId());
            member.put("nickname", m.getUserInfo().getNickname());
            member.put("role", m.getRole());

            memberList.add(member);
        }
        return memberList;
    }

    @Override
    public List<Map<String,Object>> getTeamCalendar(Long teamId) {

        List<Map<String,Object>> calendarList = new ArrayList<>();
        List<Calendar> calendars = calendarRepository.findByTeam_id(teamId);

        for (Calendar c : calendars){

            Map<String,Object> calendar = new HashMap<>();

            calendar.put("date", c.getDate());
            calendar.put("content", c.getContent());

            calendarList.add(calendar);
        }
        return calendarList;
    }

    @Override
    public List<Map<String,Object>> getTeamRegister(Long teamId) {

        List<Map<String, Object>> registerList = new ArrayList<>();
        List<TeamRegister> registers = teamRegisterRepository.findByTeam_Id(teamId);

        for (TeamRegister r : registers){

            Map<String, Object> register = new HashMap<>();

            register.put("user_id", r.getUserInfo().getId());
            register.put("user_nickname", r.getUserInfo().getNickname());
            register.put("user_birth", r.getUserInfo().getBirthday());
            register.put("user_gender", r.getUserInfo().getGender());
            register.put("user_phone", r.getUserInfo().getPhone());
            register.put("user_email", r.getUserInfo().getEmail());
            register.put("content", r.getContent());

            registerList.add(register);
        }
        return registerList;
    }

    @Override
    public String createCalendar(Long userId, Long teamId, Calendar calendar) {//사용자 권한 인증해야함

        Optional<Team> rawTeam = teamRepository.findById(teamId);
        String successCreateCalender = "";

        if (rawTeam.isPresent()){

            Team team = rawTeam.get();
            if (team.getUserInfo().getId() == userId) {

                Calendar calendar1 = calendarRepository.findByTeamIdAndDate(team.getId(),calendar.getDate());                 //동일한 날짜의 수정된 content가 들어왔을 경우도 있기때문에

                if (calendar1 != null) {                                        //해당 날짜의 일정 내용이 존재한다면 업데이트

                    calendar1.setContent(calendar.getContent());
                    calendarRepository.save(calendar1);
                    successCreateCalender = "일정 수정 완료";

                } else {                                                         //해당 날짜의 일정이 존재하지 않다면 새로 만들기

                    calendar.setTeam(team);
                    calendarRepository.save(calendar);
                    successCreateCalender = "일정 생성 완료";

                }

            }else {                                                                 //일정 생성 권한이 없음

                successCreateCalender = "일정 생성 권한 없음.";

            }
        }

        return successCreateCalender;
    }

    @Override
    public boolean createTeamRegister(Map<String, Object> map,UserInfo userInfo) {

        Optional<Team> team = teamRepository.findById(Long.valueOf((String) map.get("teamId")));
        TeamRegister teamRegister = teamRegisterRepository.findByUserInfo(userInfo);

        if (teamRegister != null) {                                 //이미 신청한 이력이 있다면
            teamRegister.setContent((String) map.get("content"));
            teamRegisterRepository.save(teamRegister);

            return true;
        } else {                                                    //처음 신청이라면
            teamRegister = TeamRegister.builder()
                    .team(team.get())
                    .userInfo(userInfo)
                    .content((String) map.get("content"))
                    .build();
            teamRegisterRepository.save(teamRegister);

            return false;
        }

    }


    @Override
    public List<Map<String, Object>> getMyTeams(UserInfo userInfo) {

        List<TeamMember> myTeams = teamMemberRepository.findByUserInfo(userInfo);

        List<Map<String, Object>> teamList = new ArrayList<>();

        if (myTeams != null) {

            for(int i=0;i<myTeams.size();i++){

                Map<String, Object> map = new HashMap<>();
                map.put("teamId", myTeams.get(i).getTeam().getId());
                map.put("title", myTeams.get(i).getTeam().getTitle());
                teamList.add(map);

            }
            return teamList;

        } else {

            return null;
        }
    }

    @Override
    public Page<Team> getTeamList(Pageable pageable) {
        Page<Team> teamList = teamRepository.findAll(pageable);
        return teamList;
    }
}
