package cn.bb.sourceideamanage.dto.front;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@ToString
public class FrontUser implements Serializable {
    private static final long serialVersionUID = 1310025532785175577L;
    private Integer userId;
    private String userName;
    private String userMsg;
    private Timestamp userCreateTime;
}
