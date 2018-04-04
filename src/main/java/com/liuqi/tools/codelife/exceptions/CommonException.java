package com.liuqi.tools.codelife.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @Author: LiuQI
 * @Created: 2018/3/17 18:01
 * @Version: V1.0
 **/
@ResponseStatus(value = HttpStatus.CONFLICT)
public class CommonException extends Exception {
    public CommonException() {
    }
    
    public CommonException(String message) {
        super(message);
    }
    
    public CommonException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public CommonException(Throwable cause) {
        super(cause);
    }
    
    public CommonException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
