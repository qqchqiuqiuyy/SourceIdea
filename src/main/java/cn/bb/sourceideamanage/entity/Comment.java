package cn.bb.sourceideamanage.entity;

import cn.bb.sourceideamanage.dto.front.ChildComment;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

@Data
@ToString
public class Comment implements Serializable {
    private static final long serialVersionUID = 4117882143172519503L;
    private Integer id;
    private Integer uid;
    private Integer ideaId;
    private String ideaName;
    private Integer userId;
    private String userName;
    private Integer parentId;
    private String parentName;
    private String content;
    private List<ChildComment> childComments;
    private Timestamp commentTime;
}
