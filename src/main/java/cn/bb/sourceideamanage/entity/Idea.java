package cn.bb.sourceideamanage.entity;

import lombok.Data;
import lombok.ToString;

import java.sql.Timestamp;

@Data
@ToString
public class Idea {
    private Integer ideaId;
    private Integer teamId;
    private Integer tagId;
    private Integer userId;
    private String ideaName;
    private String ideaMsg;
    private Integer ideaSupports;
    private Timestamp ideaCreateTime;
    private String ideaState;
}
