package cn.bb.sourceideamanage.dto.back;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class BackTeamMember {
    private String userName;
    private String teamRoleName;
    private String memberJoinTime;
}
