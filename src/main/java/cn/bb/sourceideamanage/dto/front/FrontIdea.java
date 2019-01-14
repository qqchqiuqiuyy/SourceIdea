package cn.bb.sourceideamanage.dto.front;

import lombok.Data;
import lombok.ToString;

import java.sql.Timestamp;

@Data
@ToString
public class FrontIdea {
    private Integer ideaId;
    private String ideaName;
    private String ideaMsg;
    private String ideaTag;
    private String ideaUserName;
    private Integer ideaSupports;
    private Timestamp ideaCreateTime;
}
