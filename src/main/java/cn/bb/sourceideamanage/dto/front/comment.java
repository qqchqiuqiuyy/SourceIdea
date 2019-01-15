package cn.bb.sourceideamanage.dto.front;

import lombok.Data;
import lombok.ToString;

import java.sql.Timestamp;

@Data
@ToString
public class comment {
    private Integer ideaId;
    private Integer userId;
    private String userName;
    private String content;
    private String ideaName;
    private Timestamp commentTime;
}
