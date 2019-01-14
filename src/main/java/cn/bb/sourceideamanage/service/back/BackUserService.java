package cn.bb.sourceideamanage.service.back;

import cn.bb.sourceideamanage.entity.Team;
import cn.bb.sourceideamanage.dto.back.BackUser;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface BackUserService {
    public PageInfo<BackUser> findUserByPage(int page, int size, String userName);
    public List<BackUser> findAllBackUser(String userName);
    public List<Team> findAllTeamByUserId(Integer userId);
}
