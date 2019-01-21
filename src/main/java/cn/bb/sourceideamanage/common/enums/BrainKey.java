package cn.bb.sourceideamanage.common.enums;

import lombok.Getter;

@Getter
public enum BrainKey {
    /**
     * 表示存储不同ideaId的用户idea
     */
    BRAIN_KEY("brainStorming:"),
    BRAIN_SUPPORT_USER("brainSupportsUsers:");

    private String key;
    BrainKey(String k){
        this.key = k;
    }
}
