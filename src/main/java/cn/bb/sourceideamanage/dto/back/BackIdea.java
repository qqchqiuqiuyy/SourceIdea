package cn.bb.sourceideamanage.dto.back;

import cn.bb.sourceideamanage.entity.Team;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

@Data
@ToString
public class BackIdea implements Serializable {
    private static final long serialVersionUID = -5726852408421000262L;
    private Integer ideaId;
    private String tagName;
    private String userName;
    private List<Team> userTeams;
    private String ideaMsg;
    private Integer ideaSupports;
    private Timestamp ideaCreateTime;
}
