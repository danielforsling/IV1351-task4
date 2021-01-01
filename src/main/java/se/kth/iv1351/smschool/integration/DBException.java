package se.kth.iv1351.smschool.integration;


/**
 *
 */
public class DBException extends Exception {

    /**
     * Create a new instance thrown because of the specified reason.
     *
     * @param message - why the exception was thrown.
     */
    public DBException(String message) {
        super(message);
    }

    /**
     * Create a new instance thrown because of the specified reason.
     *
     *
     * @param message - why the exception was thrown.
     * @param cause the original exception that will be thrown
     */
    public DBException(String message, Throwable cause) {
        super(message, cause);
    }
}
