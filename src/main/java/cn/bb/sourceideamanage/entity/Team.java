package cn.bb.sourceideamanage.entity;

import lombok.Data;
import lombok.ToString;

import java.sql.Timestamp;

@Data
@ToString
public class Team {
    private Integer teamId;
    private String teamName;
    private String teamMsg;
    private Integer teamNums;
    private Timestamp teamCreateTime;

}
