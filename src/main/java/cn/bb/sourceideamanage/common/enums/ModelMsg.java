package cn.bb.sourceideamanage.common.enums;

import lombok.Getter;

@Getter
public enum ModelMsg {
    /**
     * 当前页
     */
    INDEX_PAGE("indexPage"),
    /**
     * 总页数
     */
    TOTAL_PAGE("totalPage"),
    TEAMS("teams"),
    TEAM("team"),
    USER_TEAMS_JSON("userteamsJson"),
    MEMBERS("members"),
    PROJECTS("projects"),
    IDEAS("ideas"),
    /**
     * 在session存用户名
     */
    USER_NAME("userName"),
    /**
     * session存userId
     */
    USER_ID("userId"),
    /**
     * 成功请求返回值的key
     */
    SUCCESS("success"),
    MSG("msg"),
    /**
     * 成功跳转页面
     */
    SUCCESS_URL("successUrl"),
    IDEA_ID("ideaId"),
    IDEA_NAME("ideaName"),
    COMMENTS("comments"),
    TAG_NAME("tagName"),
    TAGS("tags"),
    IDEAS_SUPPORTS("ideasSupports"),
    IDEA_MSG("ideaMsg"),
    BRAINS("brains"),
    PROJECT_MSG("projectMsg"),
    PROJECTS_NAME("projectsName"),
    INVITES("invites"),
    USER("user"),
    TEAM_NAME("teamName"),
    TEAM_ID("teamId"),
    MANAGER("manager"),
    APPLIES("applies"),
    USERS("users"),
    FLAG("flag"),
    USER_MSG("userMsg"),
    PROJECT_NAME("projectName"),
    ;

     String msg;
    ModelMsg(String msg){
        this.msg = msg;
    }
}
