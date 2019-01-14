package cn.bb.sourceideamanage.dto.front;

import lombok.Data;
import lombok.ToString;

import java.sql.Timestamp;

@Data
@ToString
public class FrontProject {
    private Integer projectId;
    private String projectName;
    private String projectManager;
    private String projectTeam;
    private Timestamp projectCreateTime;
}
