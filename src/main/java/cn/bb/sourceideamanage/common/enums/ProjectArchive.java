package cn.bb.sourceideamanage.common.enums;

import lombok.Getter;

@Getter
public enum ProjectArchive {
    FINISH(1,"项目已完成"),
    NOTFINISH(0,"项目暂未完成");
    private Integer archive; //1表示归档 0表示未归档
    private String msg;
    ProjectArchive (Integer archive,String msg){
        this.archive =archive;
        this.msg = msg;
    }
}
