package com.example.TeamUP.Service;

import com.example.TeamUP.DTO.*;
import com.example.TeamUP.Entity.*;
import com.example.TeamUP.Entity.Calendar;
import com.example.TeamUP.Repository.*;
import com.example.TeamUP.Repository.read.TeamReadRepository;
import com.example.TeamUP.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService{

    private final TeamReadRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;
    private final TeamRegisterRepository teamRegisterRepository;
    private final CalendarRepository calendarRepository;

    /**
     * 특정 팀에 합류하는 함수
     */
    @Override
    public void joinTeam(Team team, UserInfo userInfo, Role role) {

        if (team.getMaxMember()>team.getCurrentMember()){

            TeamMember teamMember = new TeamMember();

            teamMember.setTeam(team);
            teamMember.setUserInfo(userInfo);
            teamMember.setRole(role.getKey());

            teamMemberRepository.save(teamMember);

            team.setCurrentMember(team.getCurrentMember()+1);

            teamRepository.save(team);
        }else {
            System.out.println("팀 꽉참(신청 내용만 삭제됨)");
        }
    }

    /**
     * 특정 회원이 팀에 신청한 신청내용을 보고 수락의 기능을 동작하는 함수
     */
    @Override
    public void acceptMember(Long teamId, Long userId) {
        if (teamRegisterRepository.existsByTeam_idAndUserInfo_id(teamId, userId)
                && !teamMemberRepository.existsByTeam_IdAndUserInfo_Id(teamId, userId)){

            System.out.println("멤버 수락 진행(신청 내용 있음, 멤버 아님)");

            TeamRegister teamRegister = teamRegisterRepository.findByTeam_IdAndUserInfo_Id(teamId, userId);
            teamRegister.setTeam(null);
            teamRegister.setUserInfo(null);
            teamRegisterRepository.delete(teamRegister);

            Optional<Team> team = teamRepository.findById(teamId);
            Optional<UserInfo> user = userRepository.findById(userId);

            if (team.isPresent() && user.isPresent()){
                joinTeam(team.get(), user.get(), Role.USER);
            }
        }else {
            System.out.println("신청 내용 없거나 이미 멤버임");
        }
    }

    /**
     * 특정 회원이 팀에 신청한 신청내용을 보고 거절의 기능을 동작하는 함수
     */
    @Override
    public void rejectMember(Long teamId, Long userId) {
        TeamRegister teamRegister = teamRegisterRepository.findByTeam_IdAndUserInfo_Id(teamId, userId);
        teamRegisterRepository.delete(teamRegister);
    }

    /**
     * 팀을 최초로 생성하는 함수
     */
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
        teamEntity.setCurrentMember(0);

        teamRepository.save(teamEntity);

        joinTeam(teamEntity, user, Role.MANAGER);

        return teamEntity;
    }

    /**
     * 팀에 태그를 생성하여 저장하는 함수
     */
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

    /**
     * 게시글의 상세내용을 리턴하는 함수, 어떤 회원이 조회하는지에 따라 해당 게시글의 팀에 속한 멤버인지 확인까지 가능함
     */
    @Override
    public ResponsePostDTO getPostInfo(Long id, Long teamId) {

        Optional<Team> team = teamRepository.findById(teamId);
        String writer = team.get().getUserInfo().getNickname();
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

    /**
     * 팀 상세내용에 표시될 팀 정보를 데이터베이스에서 찾아오는 함수
     */
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

    /**
     * 팀 상세내용에 표시될 팀에 속한 멤버들을 데이터베이스에서 찾아오는 함수
     */
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

    /**
     * 팀 상세내용에 표시될 일정을 데이터베이스에서 일정을 찾아오는 함수
     */
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

    /**
     *  팀에 신청한 현황들을 반환함 (팀 상세내용)
     */
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

    /**
     * 팀 상세내용에 표시할 캘린더(일정)을 생성하는 함수
     */
    @Override
    public String createCalendar(Long userId, Long teamId, Calendar calendar) {//사용자 권한 인증해야함

        Optional<Team> rawTeam = teamRepository.findById(teamId);
        String successCreateCalendar = "";

        if (rawTeam.isPresent()){

            Team team = rawTeam.get();

            if (team.getUserInfo().getId() == userId) {
                Calendar existingCalendar = calendarRepository.findByTeamIdAndDate(team.getId(), calendar.getDate());                 //동일한 날짜의 수정된 content가 들어왔을 경우도 있기때문에

                if (existingCalendar != null) {                                        //해당 날짜의 일정 내용이 존재한다면 업데이트
                    existingCalendar.setContent(calendar.getContent());
                    calendarRepository.save(existingCalendar);
                    successCreateCalendar = "일정 수정 완료";

                } else {                                                         //해당 날짜의 일정이 존재하지 않다면 새로 만들기
                    calendar.setTeam(team);
                    calendarRepository.save(calendar);
                    successCreateCalendar = "일정 생성 완료";
                }

            }else {                                                                 //일정 생성 권한이 없음
                successCreateCalendar = "일정 생성 권한 없음.";
            }
        }

        return successCreateCalendar;
    }

    /**
     * 특정 팀에게 가입신청을 하여 데이터베이스에 저장하거나 수정하는 함수
     */
    @Override
    public String createTeamRegister(Map<String, Object> map,UserInfo userInfo) {

        TeamRegister teamRegister = teamRegisterRepository.findByUserInfo(userInfo);
        TeamMember teamMember = teamMemberRepository.findByUserInfo_IdAndTeam_Id(userInfo.getId(),Long.valueOf((String) map.get("team_id")));

        if (teamMember != null) {

            return "이미 팀원";
        }

        if (teamRegister != null) {                                 //이미 신청한 이력이 있다면

            teamRegister.setContent((String) map.get("content"));
            teamRegisterRepository.save(teamRegister);

            return "신청 내용 수정 완료";

        } else {                                                    //처음 신청이라면
            Optional<Team> team = teamRepository.findById(Long.valueOf((String) map.get("team_id")));

            teamRegister = TeamRegister.builder()
                    .team(team.get())
                    .userInfo(userInfo)
                    .content((String) map.get("content"))
                    .build();
            teamRegisterRepository.save(teamRegister);

            return "신청 완료";

        }

    }

    /**
     * Mypage에서 내정보를 조회할 때 내가 속한 팀을 확인하기 위해 사용하는 함수
     */
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

    /**
     * 게시판에 팀 전체 리스트를 보여주기 위해 전체 팀을 DB에서 찾아오는 함수
     */
    @Override
    public Page<Team> getTeamList(Pageable pageable) {
        Page<Team> teamList = teamRepository.findAll(pageable);
        return teamList;
    }

    /**
     * 게시판에 팀 카테고리별 리스트를 보여주기 위해 전체 팀을 DB에서 찾아오는 함수
     */
    @Override
    public Page<Team> getTeamList(Pageable pageable, String category) {
        Page<Team> teamList = teamRepository.findByCategory(pageable, category);
        return teamList;
    }

    /**
     * 존재하는 팀을 삭제하는 메서드
     */
    @Override
    @Transactional
    public void deleteTeam(Long teamId, UserInfo userInfo) {

        try {
            Team findTeam = teamRepository.findById(teamId).get();
            if (!findTeam.getUserInfo().getId().equals(userInfo.getId())) {
                throw new CustomException(HttpStatus.UNAUTHORIZED, "팀장 만이 팀을 삭제할 수 있습니다");
            }
            teamRepository.deleteById(teamId);
        } catch (NoSuchElementException e) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "존재하지 않는 팀은 삭제할 수 없습니다.");
        }

    }

    /**
     * 들어온 태그로 게시물을 검색하는 메서드
     * @param requestTags
     * @return
     */
    @Override
    public ResponseTagsInTeam getTagsInTeam(RequestTags requestTags, Pageable pageable) {
        List<String> tags = new ArrayList<>();
        for (int i = 0; i < requestTags.getTags().size(); i++) {
            String tag = requestTags.getTags().get(i);
            tags.add(tag);
        }
        List<Tag> findTags = tagRepository.searchTagInTeam(tags);
        List<Long> teamIds = new ArrayList<>();

        ResponseTagsInTeam responseTagsInTeam = getTagsInTeam(pageable, findTags, teamIds);

        if (responseTagsInTeam != null) return responseTagsInTeam;
        return null;
    }

    private ResponseTagsInTeam getTagsInTeam(Pageable pageable, List<Tag> findTags, List<Long> teamIds) {
        if (findTags != null) {
            for (Tag findTag : findTags) {
                teamIds.add(findTag.getTeam().getId());
            }
            Page<Team> findTeams = teamRepository.searchTeams(teamIds, pageable);         //페이지 쿼리로 받아야댐
            if (findTeams != null) {
                List<Map<String, Object>> maps = new ArrayList<>();
                for (Team findTeam : findTeams) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("team_id", findTeam.getId());
                    map.put("title", findTeam.getTeamName());
                    map.put("category", findTeam.getCategory());
                    map.put("writer", findTeam.getUserInfo().getNickname());
                    maps.add(map);
                }

                ResponseTagsInTeam responseTagsInTeam = new ResponseTagsInTeam();           //토탈 페이지 추가
                responseTagsInTeam.setTotal_page(findTeams.getTotalPages());
                responseTagsInTeam.addPosts(maps);

                return responseTagsInTeam;
            }
        }
        return null;
    }

}
