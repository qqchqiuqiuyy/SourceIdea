package cn.bb.sourceideamanage.dto.back;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class BackTeamMember implements Serializable {
    private static final long serialVersionUID = 2238807752098590913L;
    private String userName;
    private String teamRoleName;
    private String memberJoinTime;
}
