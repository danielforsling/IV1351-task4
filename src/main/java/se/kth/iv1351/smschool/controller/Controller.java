/*
 * @author Daniel Forsling 2021-01-01
 */

package se.kth.iv1351.smschool.controller;

import se.kth.iv1351.smschool.integration.SMschoolDB;
import se.kth.iv1351.smschool.integration.DBException;
import se.kth.iv1351.smschool.model.Instrument;
import se.kth.iv1351.smschool.model.Rental;
import se.kth.iv1351.smschool.model.RetrieveDataException;
import se.kth.iv1351.smschool.model.Student;

import java.util.List;

/**
 *  The controller of the application. All calls to model passes through here.
 *  The constroller calls the object which calls the database.
 */
public class Controller {

    private final SMschoolDB schoolDatabase;

    /**
     * Constructor. Establish connection with the database.
     *
     * @throws DBException if failed to connect to database.
     */
    public Controller() throws DBException {
        this.schoolDatabase = new SMschoolDB();
    }

    /**
     * Searches for all available instruments, i.e. the ones that are not
     * rented out.
     *
     * @return All instruments as a List.
     * @throws RetrieveDataException if failed to search for available instruments
     */
    public List<? extends Instrument> getAllAvailableInstruments()
    throws RetrieveDataException{
        try {
            return schoolDatabase.listAllInstruments();
        } catch (DBException e) {
            throw new RetrieveDataException("Failed to retrieve instruments",e);
        }
    }

    /**
     * Searches for a specified student
     *
     * @param id the ID of the student
     * @return The Student-object
     * @throws RetrieveDataException if failed to search for student.
     */
    public Student findStudent(int id) throws RetrieveDataException {
        try {
            return schoolDatabase.findStudent(id);
        } catch (DBException e) {
           throw new RetrieveDataException("Failed to retrieve student", e);
        }
    }

    /**
     * Searches for all active rentals from a specified student.
     *
     * @param studentID the ID of the student
     * @return all rentals as a list, if no rentals are found, the list is empty.
     * @throws RetrieveDataException if failed to retrive rentals
     */
    public List getActiveRentalsForStudent(int studentID)
                                            throws RetrieveDataException{
        try {
            return schoolDatabase.listActiveRentalsForStudent(studentID);
        } catch (DBException e) {
           throw new RetrieveDataException("Failed to retrieve rentals", e);
        }
    }

    /**
     * Counts the number of active rentals for a specified student.
     *
     * @param studentID the ID of the student.
     * @return the number of rentals.
     * @throws RetrieveDataException if failed to count rentals.
     */
    public int getNumberOfActiveRentals(int studentID)
                                                throws RetrieveDataException {
        try {
            return schoolDatabase.countStudentActiveRentals(studentID);
        } catch (DBException e) {
           throw new RetrieveDataException("Failed to retrieve number of rentals", e);
        }
    }

    /**
     *  Check if a specified instrument is available for rental
     *
     * @param instrumentID the ID of the instrument.
     * @return true if instrument is available, else false
     * @throws RetrieveDataException if failed to check if instrument is available
     */
    public boolean instrumentIsAvailable(int instrumentID)
                                                throws RetrieveDataException {
        try {
            return schoolDatabase.instrumentIsAvailable(instrumentID);
        } catch (DBException e) {
           throw new RetrieveDataException("Failed to check if instruments is available", e);
        }
    }

    /**
     * Rents an instrument for a specified student.
     *
     * @param studentID the ID of the student.
     * @param date the date of the ending of rental.
     * @param instr the id of the instrument.
     * @throws RetrieveDataException if failed to rent instrument
     */
    public void studentRentInstrument(int studentID, String date, int instr)
                                                throws RetrieveDataException {
        try {
            schoolDatabase.studentRentInstrument(studentID, date, instr );
        } catch (DBException e) {
            throw new RetrieveDataException("Failed to rent instrument" , e);
        }
    }

    /**
     * Terminates the rental for a student, i.e. sets the lease_end column
     * to the current date.
     *
     * @param rental the rental object to be terminated
     * @throws RetrieveDataException if failed to terminate rental.
     */
    public void studentTerminateRental(Rental rental) throws RetrieveDataException {
        try {
            schoolDatabase.studentTerminateRental(rental);
        } catch (DBException e) {
            throw new RetrieveDataException("Failed to terminate rental", e);
        }
    }
}
