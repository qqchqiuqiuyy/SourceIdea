package cn.bb.sourceideamanage.common.enums;

import lombok.Getter;

/**
 * cache缓存失效的时间
 * @author
 */
@Getter
public enum TimeOut {
    /**
     *
     */
    TEST(10L,"测试"),
    SHORTEST(30L,"最短"),
    MEDIUM(60L,"中等"),
    LONGEST(120L,"最长"),
    FOREVER(9999L,"超长");
    public  final Long time;
    public final String cacheName;
    TimeOut(Long time,String cacheName){
        this.time = time;
        this.cacheName = cacheName;
    }
}
