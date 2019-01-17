package cn.bb.sourceideamanage.common.enums;

import lombok.Getter;

@Getter
public enum Roles {
    /**
     *
     */
    UserCommon(1,"user:common","用户"),
    UserProjectMember(2,"user:projectMember","项目成员"),
    UserProjectManager(3,"user:projectManager","项目管理员"),
    UserTeamMember(4,"user:teamMember","团队成员"),
    UserTeamManager(5,"user:teamManager","团队队长"),
    Administrator(999,"Administrator","超级管理员");

    private Integer roleId;
    private String roleName;
    private String roleMsg;
     Roles(Integer roleId,String roleName,String roleMsg){
        this.roleId = roleId;
        this.roleName = roleName;
        this.roleMsg = roleMsg;
    }
}
