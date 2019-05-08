package com.lcg.base.entity;

import javax.persistence.Id;
import java.util.Date;

/**
 * Created by johnny on 2019/5/8.
 * 实体基础类
 *
 * @author johnny
 */
public class BaseAuditableEntity<T> extends BaseEntity {
    @Id
    private T id;
    private String createUser;
    private Date createTime;
    private String updateUser;
    private Date updateTime;
    private String remark;


    public T getId() {
        return id;
    }

    public void setId(T id) {
        this.id = id;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
