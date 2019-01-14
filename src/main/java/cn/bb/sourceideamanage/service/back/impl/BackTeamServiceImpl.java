package cn.bb.sourceideamanage.service.back.impl;

import cn.bb.sourceideamanage.dao.back.BackTeamMapper;
import cn.bb.sourceideamanage.dto.back.BackTeam;
import cn.bb.sourceideamanage.dto.back.BackTeamMember;
import cn.bb.sourceideamanage.entity.Idea;
import cn.bb.sourceideamanage.entity.Project;
import cn.bb.sourceideamanage.service.back.BackTeamService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
@Service
public class BackTeamServiceImpl implements BackTeamService {
    @Resource
    BackTeamMapper mapper;

    @Override
    public List<Project> findAllProjectByTeamId(Integer teamId){
        return mapper.findAllProjectByTeamId(teamId);
    }

    @Override
    public List<BackTeamMember> findAllMemberByTeamId(Integer teamId) {
        return mapper.findAllMemberByTeamId(teamId);
    }

    @Override
    public List<Idea> findAllTeamIdea(Integer teamId) {
        return mapper.findAllTeamIdea(teamId);
    }

    @Override
    public String findTeamCaptainByTeamId(Integer teamId) {
        return mapper.findTeamCaptainByTeamId(teamId);
    }


    @Override
    public PageInfo<BackTeam> findTeamByPage(int page, int size, String teamName) {
        PageHelper.startPage(page,size);
        List<BackTeam> backTeams = mapper.findAllBackTeamMember(teamName);
        return new PageInfo<>(backTeams);
    }
}
