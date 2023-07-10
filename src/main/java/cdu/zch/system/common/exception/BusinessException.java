package cdu.zch.system.common.exception;

import cdu.zch.system.common.result.IResultCode;
import lombok.Getter;

/**
 * @author Zch
 * @date 2023/7/10
 **/
@Getter
public class BusinessException extends RuntimeException{

    public IResultCode resultCode;

    public BusinessException(IResultCode resultCode) {
        super(resultCode.getMsg());
        this.resultCode = resultCode;
    }

    public BusinessException(String message){
        super(message);
    }

    public BusinessException(String message, Throwable cause){
        super(message, cause);
    }

    public BusinessException(Throwable cause){
        super(cause);
    }
}
