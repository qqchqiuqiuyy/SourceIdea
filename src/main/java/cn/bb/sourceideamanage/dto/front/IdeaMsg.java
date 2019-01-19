package cn.bb.sourceideamanage.dto.front;

import com.sun.tools.corba.se.idl.constExpr.Times;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@ToString
public class IdeaMsg implements Serializable {
    private static final long serialVersionUID = -5953294433162576148L;
    private String ideaMsg;
    private String ideaName;
    private String ideaUser;
    private Timestamp ideaCreateTime;
}
