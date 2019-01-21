package cn.bb.sourceideamanage.common.enums;

import lombok.Getter;


/**
 * @author b
 */
@Getter
public enum ProjectArchive {
    /**
     * 完成就是1
     */
    FINISH(1,"项目已完成"),
    /**
     * 未完成就是0
     */
    NOTFINISH(0,"项目暂未完成");

    /**
     * 用来标记是否完成  1表示完成 0表示未完成
     */
    private Integer archive;
    /**
     * 信息
     */
    private String msg;

    /**
     * 构造函数
     * @param archive
     * @param msg
     */
    ProjectArchive (Integer archive,String msg){
        this.archive =archive;
        this.msg = msg;
    }
}
