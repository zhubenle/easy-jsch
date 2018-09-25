package com.t0mpi9.client.exception;

/**
 * <br/>
 * Created on 2018/9/25 12:07.
 *
 * @author zhubenle
 */
public class JschClientException extends RuntimeException{
    private static final long serialVersionUID = 1593384926137107300L;

    public JschClientException() {
        super();
    }

    public JschClientException(String message) {
        super(message);
    }

    public JschClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public JschClientException(Throwable cause) {
        super(cause);
    }

    protected JschClientException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
