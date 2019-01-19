package cn.bb.sourceideamanage.dto.front;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class ProjectMember implements Serializable {
    private static final long serialVersionUID = 5650448704145115975L;
    private String userName;
    private String projectRole;
}
