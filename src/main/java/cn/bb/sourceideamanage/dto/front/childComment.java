package cn.bb.sourceideamanage.dto.front;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

@Data
@ToString
public class childComment implements Serializable {
    private static final long serialVersionUID = -6383919776860160881L;
    private Integer id;
    private Integer uid;
    private Integer ideaId;
    private String ideaName;
    private Integer userId;
    private String userName;
    private Integer parentId;
    private String parentName;
    private String content;
    private Timestamp commentTime;
}
