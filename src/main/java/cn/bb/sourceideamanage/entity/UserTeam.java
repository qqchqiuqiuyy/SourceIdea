package cn.bb.sourceideamanage.entity;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@ToString
public class UserTeam implements Serializable {
    private static final long serialVersionUID = -5828453467956851914L;
    private Integer id;
    private Integer teamId;
    private Integer roleId;
    private Integer userId;
    private Timestamp memberJoinTime;
}
