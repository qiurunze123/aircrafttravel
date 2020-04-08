package com.travel.function.exception;

import com.travel.commons.enums.ResultStatus;

/**
 * @author 邱润泽 bullock
 */
public class DeductStockException extends RuntimeException {

    private ResultStatus status;

    public DeductStockException(ResultStatus status){
        super();
        this.status = status;
    }

    public ResultStatus getStatus() {
        return status;
    }

    public void setStatus(ResultStatus status) {
        this.status = status;
    }
}
