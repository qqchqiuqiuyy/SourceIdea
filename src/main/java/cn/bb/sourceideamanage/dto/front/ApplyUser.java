package cn.bb.sourceideamanage.dto.front;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@ToString
public class ApplyUser implements Serializable {
    private static final long serialVersionUID = 8926939858670294477L;
    private Integer teamId;
    private Integer userId;
    private String userName;
    private Timestamp applyTime;
}
