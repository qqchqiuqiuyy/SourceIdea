package cn.bb.sourceideamanage.common.enums;

import lombok.Getter;


@Getter
public enum TimeOut {
    /**
     *
     */
    TeamPage(60L,"teamPage"),
    IdeaPage(60L,"ideaPage"),
    ProjectPage(60L,"projectPage"),
    IdeaSupportsList(60L,"supportsList"),
    Tags(3600L,"allTag"),
    DefaultTime(60L,"DefaultTime"),
    IdeaMsg(60L,"ideaMsg"),
    MyIdeas(60L,"myIdeas"),
    Comments(60L,"comments"),
    UserMsg(60L,"userMsg"),
    InviteList(60L,"inviteList"),
    MyTeams(60L,"myTeams"),
    MyProjects(60L,"myProjects"),
    ProjectMsg(60L,"projectMsg"),
    BackIdeasPage(60L,"backIdeasPage"),
    AllProject(60L,"allProject"),
    AllProjectsByUserId(60L,"allProjectsByUserId"),
    AllMemberByteamId(60L,"allMemberByteamId"),
    AllteamIdeas(60L,"allteamIdeas"),
    AllTeamByUserId(60L,"allTeamByUserId"),
    MyTeamMember(60L,"myTeamMember"),
    TeamRole(60L,"teamRole"),
    TeamId(300L,"teamId"),
    AllTeamMsg(60L,"allTeamMsg"),
    TeamIdeas(60L,"teamIdeas"),
    MyProject(60L,"myProject");

    public  final Long time;
    public final String cacheName;
    TimeOut(Long time,String cacheName){
        this.time = time;
        this.cacheName = cacheName;
    }
}
