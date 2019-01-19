package cn.bb.sourceideamanage.entity;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@ToString
public class Team implements Serializable {
    private static final long serialVersionUID = -2841673186420328885L;
    private Integer teamId;
    private String teamName;
    private String teamMsg;
    private Integer teamNums;
    private Timestamp teamCreateTime;

}
