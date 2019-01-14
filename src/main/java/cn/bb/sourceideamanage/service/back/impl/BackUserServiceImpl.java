package cn.bb.sourceideamanage.service.back.impl;

import cn.bb.sourceideamanage.dao.back.BackUserMapper;
import cn.bb.sourceideamanage.dto.back.BackUser;
import cn.bb.sourceideamanage.entity.Team;
import cn.bb.sourceideamanage.service.back.BackUserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class BackUserServiceImpl implements BackUserService {

    @Resource
    BackUserMapper adminMapper;

    @Override
    public List<BackUser> findAllBackUser(String userName){
       return  adminMapper.findAllBackUser(userName);
    }

    @Override
    public List<Team> findAllTeamByUserId(Integer userId) {
        return adminMapper.findAllTeamByUserId(userId);
    }

    /*@Override
    public PageInfo<BackUser> searchBackUser(int page, int size,String userName) {
        PageHelper.startPage(page,size);
        List<BackUser> back_users = adminMapper.searchBackUser(userName);
        return new PageInfo<>(back_users);
    }*/

    @Override
    public PageInfo<BackUser> findUserByPage(int page, int size, String userName){
        PageHelper.startPage(page,size);
        //搜索
        List<BackUser> users = adminMapper.findAllBackUser(userName);
        return new PageInfo<>(users);
    }
}
