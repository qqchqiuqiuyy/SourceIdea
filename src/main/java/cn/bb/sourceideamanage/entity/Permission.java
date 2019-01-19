package cn.bb.sourceideamanage.entity;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class Permission implements Serializable {
    private static final long serialVersionUID = 3801034467034921875L;
    private Integer permissionId;
    private Integer roleId;
    private String permissionName;
}
