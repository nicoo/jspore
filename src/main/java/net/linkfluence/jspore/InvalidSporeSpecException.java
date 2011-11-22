/*
 */
package net.linkfluence.jspore;

/**
 * Exception thrown when the spec cannot be read or
 * is not valid (missing parameter).
 * 
 * @author Nicolas Yzet <nyzet@linkfluence.net>
 */
public class InvalidSporeSpecException extends Exception {

    public InvalidSporeSpecException(String message) {
        super(message);
    }

    public InvalidSporeSpecException(Throwable cause) {
        super(cause);
    }

    public InvalidSporeSpecException(String message, Throwable cause) {
        super(message, cause);
    }
}
