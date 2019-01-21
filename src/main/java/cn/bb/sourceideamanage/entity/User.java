package cn.bb.sourceideamanage.entity;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@ToString
@Accessors(chain = true)
public class User implements Serializable {
    private static final long serialVersionUID = -4731662733683909033L;
    private Integer userId;
    private String userName;
    private String userMsg;
    private String userAccount;
    private String userPassword;
    private Timestamp userCreateTime;

}
