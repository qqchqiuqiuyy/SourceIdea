package cn.bb.sourceideamanage.dto.front;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.sql.Timestamp;


/**
 * @author user
 */
@Data
@ToString
public class TeamMsg implements Serializable {
    private static final long serialVersionUID = 5517363151354586319L;
    private Integer teamId;
    private String teamName;
    private String teamMsg;
    private Timestamp teamCreateTime;
    private Integer teamNums;
}
