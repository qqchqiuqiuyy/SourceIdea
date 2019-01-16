package cn.bb.sourceideamanage.dto.front;

import lombok.Data;
import lombok.ToString;

import java.sql.Timestamp;

@Data
@ToString
public class frontUser {
    private Integer userId;
    private String userName;
    private String userMsg;
    private Timestamp userCreateTime;
}
