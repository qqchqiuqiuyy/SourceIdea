package cn.bb.sourceideamanage.dto.front;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

@Data
@ToString
public class FrontProjectMsg implements Serializable {
    private static final long serialVersionUID = 5956771209807443769L;
    private Integer projectId;
    private String projectName;
    private String projectMsg;
    private String teamName;
    private Timestamp projectCreateTime;
}
