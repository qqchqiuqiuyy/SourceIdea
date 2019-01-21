package cn.bb.sourceideamanage.entity;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@ToString
public class CommentIdea implements Serializable {
    private static final long serialVersionUID = 270055613039150909L;
    private Integer id;
    private Integer ideaId;
    private Integer userId;
    private String content;
    private Timestamp commentTime;
}
