package cn.bb.sourceideamanage.dto.front;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@ToString
public class FrontProject implements Serializable {
    private static final long serialVersionUID = 6995925914975953786L;
    private Integer projectId;
    private String projectName;
    private String projectManager;
    private String projectTeam;
    private Timestamp projectCreateTime;
}
