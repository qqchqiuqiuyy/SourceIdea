package cn.bb.sourceideamanage.dto.front;

import lombok.Data;
import lombok.ToString;

import java.sql.Timestamp;

@Data
@ToString
public class MyTeamMember {
    private Integer userId;
    private String userName;
    private String teamRole;
    private Timestamp memberJoinTime;
}
