package cn.bb.sourceideamanage.entity;

import lombok.Data;
import lombok.ToString;

import java.sql.Timestamp;

@Data
@ToString
public class UserTeam {
    private Integer id;
    private Integer teamId;
    private Integer roleId;
    private Integer userId;
    private Timestamp memberJoinTime;
}
