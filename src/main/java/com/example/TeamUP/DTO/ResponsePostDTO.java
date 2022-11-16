package com.example.TeamUP.DTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@ApiModel(value = "ResponsePostDTO : 게시글 상세내용 API", description = "게시글 상세내용")
@Data
public class ResponsePostDTO {
    @ApiModelProperty(value = "게시글 번호 (팀 번호)")
    Long teamId;

    @ApiModelProperty(value = "게시글 제목")
    String title;

    @ApiModelProperty(value = "게시글 내용")
    String content;

    @ApiModelProperty(value = "게시글 작성자 (팀장)")
    String writer;

    @ApiModelProperty(value = "팀 최대인원")
    int max_member;

    @ApiModelProperty(value = "팀 현재인원")
    int current_member;

    @ApiModelProperty(value = "팀에 속한 유저 목록")
    List<Map<String, Object>> team_member;

    @ApiModelProperty(value = "해당 팀에 조회한 사람의 지원 현황")
    Boolean registered;
}
