package cn.bb.sourceideamanage.dto.front;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@ToString
public class TeamMember implements Serializable {
    private static final long serialVersionUID = 5744183070662356714L;
    private String userName;
    private Timestamp memberJoinTime;
    private String teamRoleName;
}
