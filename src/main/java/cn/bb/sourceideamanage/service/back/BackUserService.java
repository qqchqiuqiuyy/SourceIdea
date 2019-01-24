package cn.bb.sourceideamanage.service.back;

import cn.bb.sourceideamanage.entity.Team;
import cn.bb.sourceideamanage.dto.back.BackUser;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface BackUserService {
    /**
     * 分页查找用户
     * @param page  当前页
     * @param size  数量
     * @param userName  用户名
     * @return
     */
    public PageInfo<BackUser> findUserByPage(int page, int size, String userName);

    /**
     * 通过用户名
     * @param userName
     * @return
     */
    public List<BackUser> findAllBackUser(String userName);
    public List<Team> findAllTeamByUserId(Integer userId);
}
