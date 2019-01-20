package cn.bb.sourceideamanage.common.enums;

import lombok.Getter;

@Getter
public enum IdeaSupportsKey {
    /**
     * 表示存储不同ideaId的用户idea
     */
    UserSetKey("ideaSupportUser:"),
    /**
     * 存储某个idea的点赞数
     */
    SupportsKey("ideaSupportNums:");
    private String key;
    IdeaSupportsKey(String k){
        this.key = k;
    }
}
