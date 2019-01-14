package cn.bb.sourceideamanage.dto.back;

import cn.bb.sourceideamanage.entity.Team;
import lombok.Data;
import lombok.ToString;

import java.sql.Timestamp;
import java.util.List;

@Data
@ToString
public class BackIdea {
    private Integer ideaId;
    private String tagName;
    private String userName;
    private List<Team> userTeams;
    private String ideaMsg;
    private Integer ideaSupports;
    private Timestamp ideaCreateTime;
}
