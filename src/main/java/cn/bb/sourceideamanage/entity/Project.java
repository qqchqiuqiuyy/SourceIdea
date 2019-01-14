package cn.bb.sourceideamanage.entity;

import lombok.Data;
import lombok.ToString;

import java.sql.Timestamp;

@Data
@ToString
public class Project {
    private Integer projectId;
    private Integer teamId;
    private String projectName;
    private String projectMsg;
    private Integer projectArchive;
    private Timestamp projectCreateTime;
}
