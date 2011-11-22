/*
 */
package net.linkfluence.jspore;

/**
 * Exception thrown by spore at runtime
 * 
 * @author Nicolas Yzet <nyzet@linkfluence.net>
 */
public class SporeException extends Exception {
   public SporeException(String message) {
        super(message);
    }

    public SporeException(Throwable cause) {
        super(cause);
    }

    public SporeException(String message, Throwable cause) {
        super(message, cause);
    } 
}
