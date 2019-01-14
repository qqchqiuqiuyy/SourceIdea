package cn.bb.sourceideamanage.entity;

import lombok.Data;
import lombok.ToString;

import java.sql.Timestamp;

@Data
@ToString
public class commentIdea {
    private Integer id;
    private Integer ideaId;
    private Integer userId;
    private String ideaComments;
    private Timestamp commentTime;
}
