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
    SHORTEST(30L,"最短"),
    MEDIUM(60L,"中等"),
    LONGEST(120L,"最长"),
    FOREVER(-1L,"永久");
    public  final Long time;
    public final String cacheName;
    TimeOut(Long time,String cacheName){
        this.time = time;
        this.cacheName = cacheName;
    }
}
