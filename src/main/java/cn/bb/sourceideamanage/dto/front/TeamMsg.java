package cn.bb.sourceideamanage.dto.front;

import lombok.Data;
import lombok.ToString;

import java.sql.Timestamp;


@Data
@ToString
public class TeamMsg {
    private Integer teamId;
    private String teamName;
    private String teamMsg;
    private Timestamp teamCreateTime;
    private Integer teamNums;
}
