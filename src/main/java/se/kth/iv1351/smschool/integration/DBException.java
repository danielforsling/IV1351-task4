/*
 *   @author Daniel Forsling 2021-01-02
 */

package se.kth.iv1351.smschool.integration;


/**
 * Thrown when a call to the database fails.
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
