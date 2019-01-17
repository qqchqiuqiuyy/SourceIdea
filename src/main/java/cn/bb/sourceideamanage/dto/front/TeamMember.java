package cn.bb.sourceideamanage.dto.front;

import lombok.Data;
import lombok.ToString;

import java.sql.Timestamp;

@Data
@ToString
public class TeamMember {
    private String userName;
    private String teamRoleName;
    private Timestamp memberJoinTime;
}
