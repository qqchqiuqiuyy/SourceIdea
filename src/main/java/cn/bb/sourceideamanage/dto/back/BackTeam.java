package cn.bb.sourceideamanage.dto.back;

import cn.bb.sourceideamanage.entity.Idea;
import cn.bb.sourceideamanage.entity.Project;
import lombok.Data;
import lombok.ToString;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Data
@ToString
public class BackTeam {
    private Integer teamId;
    private String teamName;
    private String teamCaptain;
    private String teamMsg;
    private Timestamp teamCreateTime;
    private Integer teamNums;
    private List<Project> teamProjects = new ArrayList<>();
    private List<BackTeamMember> backTeamMembers = new ArrayList<>();
    private List<Idea> teamIdeas  = new ArrayList<>();
}
