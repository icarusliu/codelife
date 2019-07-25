package com.liuqi.tools.codelife.util.exceptions;

import org.springframework.security.core.AuthenticationException;

/**
 * 授权失败异常
 *
 * @author LiuQI 2019/5/17 16:35
 * @version V1.0
 **/
public class RealAuthenticationException extends AuthenticationException {
    /**
     * Constructs an {@code AuthenticationException} with the specified message and root
     * cause.
     *
     * @param msg the detail message
     * @param t   the root cause
     */
    public RealAuthenticationException(String msg, Throwable t) {
        super(msg, t);
    }

    /**
     * Constructs an {@code AuthenticationException} with the specified message and no
     * root cause.
     *
     * @param msg the detail message
     */
    public RealAuthenticationException(String msg) {
        super(msg);
    }
}
