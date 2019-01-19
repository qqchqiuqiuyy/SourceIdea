package cn.bb.sourceideamanage.dto.front;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@ToString
public class FrontIdea implements Serializable {
    private static final long serialVersionUID = 1521939273292663110L;
    private Integer ideaId;
    private String ideaName;
    private String ideaMsg;
    private String ideaTag;
    private String ideaUserName;
    private Integer teamId;
    private Integer ideaSupports;
    private Timestamp ideaCreateTime;
}
