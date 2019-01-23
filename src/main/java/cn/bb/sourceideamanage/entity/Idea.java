package cn.bb.sourceideamanage.entity;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@ToString
public class Idea implements Serializable {
    private static final long serialVersionUID = 5574610433433407369L;
    private Integer ideaId;
    private Integer teamId;
    private Integer tagId;
    private Integer userId;
    private String ideaName;
    private String ideaMsg;
    private Integer ideaSupports;
    private Timestamp ideaCreateTime;
}
