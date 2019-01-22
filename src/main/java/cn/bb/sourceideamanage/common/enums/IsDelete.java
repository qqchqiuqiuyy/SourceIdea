package cn.bb.sourceideamanage.common.enums;

import lombok.Getter;

@Getter
public enum IsDelete {

    /**
     * 已删除
     */
    DELETE(1,"已删除"),
    /**
     * 未删除
     */
    NOTDELETE(0,"未删除");

    /**
     * 如果是1表示已删除
     * 2表示未删除
     */
    private Integer isDelete;
    /**
     * 说明信息
     */
    private String msg;

    /**
     * 构造函数
     * @param isDelete
     * @param msg
     */
    IsDelete(Integer isDelete,String msg){
        this.isDelete = isDelete;
        this.msg = msg;
    }
}
