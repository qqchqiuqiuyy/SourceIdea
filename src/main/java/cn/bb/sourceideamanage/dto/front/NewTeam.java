package cn.bb.sourceideamanage.dto.front;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@ToString
@Accessors(chain = true)
public class NewTeam implements Serializable {
    private static final long serialVersionUID = -7140787864228949714L;
    private String teamName;
    private Integer teamId;
    private String teamMsg;
}
