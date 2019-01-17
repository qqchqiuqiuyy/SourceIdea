package cn.bb.sourceideamanage.entity;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@ToString
@Accessors(chain = true)
public class Role {
    private Integer roleId;
    private String roleName;
    private String roleMsg;
}
