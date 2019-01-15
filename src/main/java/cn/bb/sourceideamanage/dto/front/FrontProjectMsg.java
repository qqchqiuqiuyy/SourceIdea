package cn.bb.sourceideamanage.dto.front;

import lombok.Data;
import lombok.ToString;

import java.sql.Timestamp;
import java.util.List;

@Data
@ToString
public class FrontProjectMsg {
    private Integer projectId;
    private String projectName;
    private String projectMsg;
    private String teamName;
    private Timestamp projectCreateTime;
    private List<ProjectMember> ProjectMembers;
}
