package cn.bb.sourceideamanage.dto.front;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@ToString
public class inviteUser implements Serializable {
    private static final long serialVersionUID = 7131831770237863322L;
    private Integer userId;
    private Integer teamId;
    private String teamName;
    private Timestamp inviteTime;
}
