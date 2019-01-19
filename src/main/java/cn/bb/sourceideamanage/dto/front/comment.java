package cn.bb.sourceideamanage.dto.front;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@ToString
public class comment implements Serializable {
    private static final long serialVersionUID = 4117882143172519503L;
    private Integer ideaId;
    private Integer userId;
    private String userName;
    private String content;
    private String ideaName;
    private Timestamp commentTime;
}
