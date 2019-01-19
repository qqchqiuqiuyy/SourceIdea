package cn.bb.sourceideamanage.entity;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@ToString
public class Project implements Serializable {
    private static final long serialVersionUID = 6494708248426648301L;
    private Integer projectId;
    private Integer teamId;
    private String projectName;
    private String projectMsg;
    private Integer projectArchive;
    private Timestamp projectCreateTime;
}
