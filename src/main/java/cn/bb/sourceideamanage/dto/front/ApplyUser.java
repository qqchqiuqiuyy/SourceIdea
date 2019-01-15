package cn.bb.sourceideamanage.dto.front;

import lombok.Data;
import lombok.ToString;

import java.sql.Timestamp;

@Data
@ToString
public class ApplyUser {
    private Integer teamId;
    private Integer userId;
    private String userName;
    private Timestamp applyTime;
}
