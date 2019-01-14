package cn.bb.sourceideamanage.service.back;

import cn.bb.sourceideamanage.dto.back.BackTeam;
import cn.bb.sourceideamanage.dto.back.BackTeamMember;
import cn.bb.sourceideamanage.entity.Idea;
import cn.bb.sourceideamanage.entity.Project;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface BackTeamService {
    public List<Project> findAllProjectByTeamId(Integer teamId);
    public List<BackTeamMember> findAllMemberByTeamId(Integer teamId);
    public List<Idea> findAllTeamIdea(Integer teamId);
    public String findTeamCaptainByTeamId(Integer teamId);
    public PageInfo<BackTeam> findTeamByPage(int page, int size, String teamName);
}
