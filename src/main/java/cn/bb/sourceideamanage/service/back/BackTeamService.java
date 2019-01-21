package cn.bb.sourceideamanage.service.back;

import cn.bb.sourceideamanage.dto.back.BackTeam;
import cn.bb.sourceideamanage.dto.back.BackTeamMember;
import cn.bb.sourceideamanage.entity.Idea;
import cn.bb.sourceideamanage.entity.Project;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface BackTeamService {
    /**
     * 根据团队Id查找所有的团队
     * @param teamId
     * @return
     */
    public List<Project> findAllProjectByTeamId(Integer teamId);

    /**
     * 根据团队Id 查找所有的团员
     * @param teamId
     * @return
     */
    public List<BackTeamMember> findAllMemberByTeamId(Integer teamId);

    /**
     * 查找所有的团队想法
     * @param teamId
     * @return
     */
    public List<Idea> findAllTeamIdea(Integer teamId);

    /**
     * 通过团队Id查找团队队长
     * @param teamId
     * @return
     */
    public String findTeamCaptainByTeamId(Integer teamId);

    /**
     * 根据团队名分页展示
     * @param page
     * @param size
     * @param teamName
     * @return
     */
    public PageInfo<BackTeam> findTeamByPage(int page, int size, String teamName);
}
