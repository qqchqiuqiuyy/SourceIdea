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

    /**
     * 根据团队id找到所有的项目
     * @param teamId
     * @return
     */
    @Override
    public List<Project> findAllProjectByTeamId(Integer teamId){
        return mapper.findAllProjectByTeamId(teamId);
    }

    /**
     * 根据团队id找到所有的成员
     * @param teamId    团队id
     * @return
     */
    @Override
    public List<BackTeamMember> findAllMemberByTeamId(Integer teamId) {
        return mapper.findAllMemberByTeamId(teamId);
    }

    /**
     * 根据团队id找到所有的团队id
     * @param teamId 团队id
     * @return
     */
    @Override
    public List<Idea> findAllTeamIdea(Integer teamId) {
        return mapper.findAllTeamIdea(teamId);
    }

    /**
     * 根据团队id找到团队队长
     * @param teamId 团队id
     * @return
     */
    @Override
    public String findTeamCaptainByTeamId(Integer teamId) {
        return mapper.findTeamCaptainByTeamId(teamId);
    }


    /**
     * 分页显示
     * @param page  当前页
     * @param size  每页数
     * @param teamName  团队名
     * @return
     */
    @Override
    public PageInfo<BackTeam> findTeamByPage(int page, int size, String teamName) {
        PageHelper.startPage(page,size);
        List<BackTeam> backTeams = mapper.findAllBackTeamMember(teamName);
        return new PageInfo<>(backTeams);
    }
}
