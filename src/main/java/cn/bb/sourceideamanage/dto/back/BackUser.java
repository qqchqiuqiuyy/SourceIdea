package cn.bb.sourceideamanage.dto.back;

import cn.bb.sourceideamanage.entity.Team;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Data
@ToString
public class BackUser implements Serializable {
    private static final long serialVersionUID = 8712663323264339918L;
    private Integer userId;
    private String userName;
    private List<Team> backTeams = new ArrayList<>();
    private String userMsg;
    private Timestamp userCreateTime;
}
