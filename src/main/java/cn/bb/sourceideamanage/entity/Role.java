package cn.bb.sourceideamanage.entity;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@ToString
@Accessors(chain = true)
public class Role implements Serializable {
    private static final long serialVersionUID = 1946488701980627088L;
    private Integer roleId;
    private String roleName;
    private String roleMsg;
}
