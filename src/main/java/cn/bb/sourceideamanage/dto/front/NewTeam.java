package cn.bb.sourceideamanage.dto.front;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@ToString
@Accessors(chain = true)
public class NewTeam {
    private String teamName;
    private Integer teamId;
    private String teamMsg;
}
