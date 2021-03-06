package cn.bb.sourceideamanage.dto.front;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.net.InetAddress;
import java.sql.Timestamp;

@Data
@ToString
public class FrontTeam implements Serializable{
    private static final long serialVersionUID = -8671491253210110063L;
    private Integer teamId;
    private String teamCaptain;
    private String teamName;
    private Timestamp teamCreateTime;
}
