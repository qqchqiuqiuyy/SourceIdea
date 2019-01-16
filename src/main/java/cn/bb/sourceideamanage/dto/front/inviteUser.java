package cn.bb.sourceideamanage.dto.front;

import lombok.Data;
import lombok.ToString;

import java.sql.Timestamp;

@Data
@ToString
public class inviteUser {
    private Integer userId;
    private Integer teamId;
    private String teamName;
    private Timestamp inviteTime;
}
