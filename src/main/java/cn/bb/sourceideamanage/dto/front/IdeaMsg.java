package cn.bb.sourceideamanage.dto.front;

import com.sun.tools.corba.se.idl.constExpr.Times;
import lombok.Data;
import lombok.ToString;

import java.sql.Timestamp;

@Data
@ToString
public class IdeaMsg {
    private String ideaMsg;
    private String ideaName;
    private String ideaUser;
    private Timestamp ideaCreateTime;
}
