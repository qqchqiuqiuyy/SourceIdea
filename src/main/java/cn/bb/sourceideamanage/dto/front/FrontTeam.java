package cn.bb.sourceideamanage.dto.front;

import lombok.Data;
import lombok.ToString;

import java.net.InetAddress;
import java.sql.Timestamp;

@Data
@ToString
public class FrontTeam {
    private Integer teamId;
    private String teamCaptain;
    private String teamName;
    private Timestamp teamCreateTime;
}
