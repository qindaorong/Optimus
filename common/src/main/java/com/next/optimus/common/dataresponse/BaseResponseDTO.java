package com.next.optimus.common.dataresponse;

/**
 * BaseResponseDTO
 *
 * @author qindaorong
 * @date 2017/10/16
 */
public class BaseResponseDTO{
    
    /**
     * Http访问返回码
     */
    protected Integer httpStatus;
    
    public int getHttpStatus() {
        return httpStatus;
    }
    
    public void setHttpStatus(int httpStatus) {
        this.httpStatus = httpStatus;
    }
    
    @Override
    public String toString() {
        return super.toString();
    }
}
