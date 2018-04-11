package com.next.optimus.common.exception;

/**
 * Created by Michael on 12/11/2017.
 */
public class CacheException extends Exception {
    public CacheException() {
        super();
    }
    
    public CacheException(String message) {
        super(message);
    }
    
    public CacheException(String message, Throwable rootCause) {
        super(message, rootCause);
    }
    
    public CacheException(Throwable rootCause) {
        super(rootCause);
    }
}
