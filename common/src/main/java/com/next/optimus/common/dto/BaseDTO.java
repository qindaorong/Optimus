package com.next.optimus.common.dto;

import java.util.Date;

/**
 * BaseDTO
 *
 * @author qindaorong
 * @date 2017/10/16
 */
public class BaseDTO {
    
    /**
     * 逻辑删除符号
     */
    private String deleteFlag;
    
    /**
     *创建时间
     */
    private Date createDate;
    
    /**
     *创建人
     */
    private String createUser;
    
    /**
     *最后修改时间
     */
    private Date lastUpdateDate;
    
    /**
     *最后修改人
     */
    private String lastUpdateUser;
    
    
    public String getDeleteFlag() {
        return deleteFlag;
    }
    
    public void setDeleteFlag(String deleteFlag) {
        this.deleteFlag = deleteFlag;
    }
    
    public Date getCreateDate() {
        return createDate;
    }
    
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    
    public String getCreateUser() {
        return createUser;
    }
    
    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }
    
    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }
    
    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }
    
    public String getLastUpdateUser() {
        return lastUpdateUser;
    }
    
    public void setLastUpdateUser(String lastUpdateUser) {
        this.lastUpdateUser = lastUpdateUser;
    }
    
    
    @Override
    public String toString() {
        return super.toString();
    }
}
