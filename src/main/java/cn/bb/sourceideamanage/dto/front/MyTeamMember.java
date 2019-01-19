package cn.bb.sourceideamanage.dto.front;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@ToString
public class MyTeamMember implements Serializable {
    private static final long serialVersionUID = -2882162659256500428L;
    private Integer userId;
    private String userName;
    private String teamRole;
    private Timestamp memberJoinTime;
}
