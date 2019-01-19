package cn.bb.sourceideamanage.entity;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class Tag implements Serializable {
    private static final long serialVersionUID = -6626428907170506798L;
    private Integer tagId;
    private String tagName;
}
