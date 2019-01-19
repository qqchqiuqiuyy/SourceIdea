package cn.bb.sourceideamanage.dto.front;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class UserTag implements Serializable {
    private static final long serialVersionUID = -5315790949985738231L;
    private Integer userId;
    private String userName;
}
