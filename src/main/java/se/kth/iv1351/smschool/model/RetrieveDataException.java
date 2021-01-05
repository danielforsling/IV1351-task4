/*
 *   @author Daniel Forsling 2021-01-02
 */
package se.kth.iv1351.smschool.model;

/**
 * Thrown when controller catches an exception from the class
 * accessing the database.
 */
public class RetrieveDataException extends Exception {

    public RetrieveDataException(String message) {
        super(message);
    }

    public RetrieveDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
