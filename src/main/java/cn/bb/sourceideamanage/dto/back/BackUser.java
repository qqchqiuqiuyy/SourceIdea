package cn.bb.sourceideamanage.dto.back;

import cn.bb.sourceideamanage.entity.Team;
import lombok.Data;
import lombok.ToString;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Data
@ToString
public class BackUser {
    private Integer userId;
    private String userName;
    private List<Team> backTeams = new ArrayList<>();
    private String userMsg;
    private Timestamp userCreateTime;
}
