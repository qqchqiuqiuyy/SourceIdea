package cn.bb.sourceideamanage.entity;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.sql.Timestamp;

@Data
@ToString
@Accessors(chain = true)
public class User {
    private Integer userId;
    private String userName;
    private String userMsg;
    private String userImg;
    private String userAccount;
    private String userPassword;
    private Timestamp userCreateTime;

}
