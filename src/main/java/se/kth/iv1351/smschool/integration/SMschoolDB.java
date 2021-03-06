/*
 *   @author Daniel Forsling 2021-01-01
 *
 * Inspiration of this java class is taken from the programs jdbc-intro and jdbc-bank
 * created by Leif Lindbäck.
 * https://github.com/KTH-IV1351/jdbc-intro
 * https://github.com/KTH-IV1351/jdbc-bank
 */

package se.kth.iv1351.smschool.integration;

import se.kth.iv1351.smschool.model.Instrument;
import se.kth.iv1351.smschool.model.Rental;
import se.kth.iv1351.smschool.model.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


/**
 * This program is simulating an application for a music school which
 * connects to their database.
 */
public class SMschoolDB {

    private static final String RENTAL_TABLE_NAME = "rental";
    private static final String RENTAL_ID = "rental_id";
    private static final String RENTAL_COL2 = "student_id";
    private static final String RENTAL_LEASE_START = "lease_start";
    private static final String RENTAL_LEASE_END = "lease_end";

    private static final String SCHOOL_INSTRUMENT_ID = "school_instrument_id";
    private static final String CURRENT_DATE = "CURRENT_DATE";

    private static final String STUDENT_PK = "student_id";

    private static final String AVAIL_INSTR_TABLE_NAME = "available_instruments";
    private static final String INSTR_TYPE_TABLE_NAME = "instrument_type";
    private static final String INST_TYPE_ID = "instrument_type_id";
    private static final String AVAILABLE = "available";

    private Connection connection;

    private PreparedStatement findActiveRentalsWithStudent;
    private PreparedStatement findAllAvailableInstrumentsStmt;


    private PreparedStatement countActiveStudentRental;
    private PreparedStatement studentRentStmt;
    private PreparedStatement findStudentStmt;
    private PreparedStatement updateInstrumentStatusStmt;
    private PreparedStatement terminateRentalStmt;

    /**
     * Constructor
     */
    public SMschoolDB() throws DBException {
        try {
            connectToDB();
            prepareStatements();

        } catch (SQLException exc) {
            throw new DBException("Could not connect to database.", exc);
        }
    }

    /**
     * Searches for a specified Student.
     *
     * @param studentID the ID of the student.
     * @return A Student object
     * @throws DBException If failed to seach for student.
     */
    public Student findStudent(int studentID) throws DBException {
        String failureMessage = "Could not search for student " + studentID;

        ResultSet result = null;

        Student student = null;

        try {
            findStudentStmt.setInt(1, studentID);
            result = findStudentStmt.executeQuery();

            if (result.next()) {
                student = new Student(
                        result.getInt(STUDENT_PK),
                        result.getString(2),
                        result.getString(3)
                );
            }
            connection.commit();
        } catch (SQLException sqlE) {
            handleException(failureMessage, sqlE);
        } finally {
            closeResultSet(failureMessage, result);
        }

        return student;
    }

    /**
     * Searches for all available instruments, i.e. the ones that are not
     * rented out.
     *
     * @return All instruments as a List.
     * @throws DBException if failed to search for available instruments
     */
    public List<Instrument> listAllInstruments() throws DBException {
        String failureMessage = "Could not search for available instruments";

        List<Instrument> instruments = new ArrayList<>();

        try (ResultSet result = findAllAvailableInstrumentsStmt.executeQuery()) {

            while (result.next()) {
                Instrument instr = new Instrument(result.getInt(1),
                        result.getString(2),
                        result.getString(3),
                        result.getInt(4));

                instruments.add(instr);
            }

            connection.commit();
        } catch (SQLException sqlE) {
            handleException(failureMessage, sqlE);
        }
        return instruments;
    }

    /**
     * Searches for all active rentals from a specified student.
     *
     * @param studentID the ID of the student
     * @return all rentals as a list, if no rentals are found, the list is empty.
     * @throws DBException if failed to retrive rentals
     */
    public List<? extends Rental> listActiveRentalsForStudent(int studentID) throws DBException {
        String failureMessage = "Could not retrieve rentals for studentID" + studentID;
        ResultSet result = null;

        List<Rental> rentals = new ArrayList<>();
        try {

            findActiveRentalsWithStudent.setInt(1, studentID);
            result = findActiveRentalsWithStudent.executeQuery();

            while (result.next()) {

                rentals.add(new Rental(result.getInt(1),
                        result.getInt(2),
                        result.getString(3),
                        result.getString(4),
                        result.getInt(5)));
            }
            connection.commit();
        } catch (SQLException sqlErr) {
            handleException(failureMessage, sqlErr);
        } finally {
            closeResultSet(failureMessage, result);
        }
        return rentals;
    }

    /**
     * Counts the number of active rentals of a specified student. Active meaning the lease_end date
     * is after the current date.
     *
     * @param studentID The id of the student.
     * @return The number of rentals as an int.
     * @throws DBException If failed to count the rentals.
     */
    public int countStudentActiveRentals(int studentID) throws DBException {
        String failureMessage = "Could not search for rentals of specified student " + studentID + ".";
        ResultSet result = null;
        int activeRentals = 0;
        try {
            countActiveStudentRental.setInt(1, studentID);
            result = countActiveStudentRental.executeQuery();

            while (result.next()) {
                activeRentals = result.getInt(1);
            }
            return activeRentals;
        } catch (SQLException sqlE) {
            handleException(failureMessage, sqlE);
        } finally {
            closeResultSet(failureMessage, result);
        }
        return activeRentals;
    }

    /**
     * Check if the specified instrument is available.
     *
     * @param instrumentID the ID of the instruments.
     * @return true if available, else false
     * @throws DBException if failed to check instrument
     */
    public boolean instrumentIsAvailable(int instrumentID) throws DBException {
        List<Instrument> list = listAllInstruments();
        for (Instrument instr : list) {
            if (instr.getId() == instrumentID)
                return true;
        }
        return false;
    }

    /**
     * Adds a rental for a specified student.
     *
     * @param studentID the ID of the student
     * @param leaseEnd the date of the return of instrument
     * @param instrumentID the ID of the instrument
     * @throws DBException
     */
    public void studentRentInstrument(int studentID, String leaseEnd, int instrumentID) throws DBException {
        String failureMessage = "Could not rent instrument to specified student " + studentID + ".";

        int updatedRows = 0;

        try {

            studentRentStmt.setInt(1, studentID);
            studentRentStmt.setDate(2, java.sql.Date.valueOf(leaseEnd));
            studentRentStmt.setInt(3, instrumentID);

            updatedRows = studentRentStmt.executeUpdate();

            if (updatedRows != 1) {
                handleException(failureMessage, null);
            }
            updateInstrumentStatus(instrumentID, false);
            connection.commit();

        } catch (SQLException | DBException sqlE) {
            handleException(failureMessage, sqlE);
        }
    }

    /**
     * Updates the status for a specified instrument if its available for rent or not
     * @param instrumentID the ID of the instrument
     * @param bool the status of the instrument. True means its available.
     * @throws DBException if failed to update instrument status.
     */
    private void updateInstrumentStatus(int instrumentID, boolean bool) throws DBException {

        String failureMessage = "Could not update status of instrument: " + instrumentID + ".";
        int updatedRows = 0;
        try {
            updateInstrumentStatusStmt.setBoolean(1, bool);
            updateInstrumentStatusStmt.setInt(2, instrumentID);
            updatedRows = updateInstrumentStatusStmt.executeUpdate();

            if (updatedRows != 1) {
                handleException(failureMessage, null);
            }

        } catch (SQLException sqlE) {
            handleException(failureMessage, sqlE);
        }
    }

    /**
     * Terminates the rental of the student, i.e. updates the lease_end column
     * to the current date.
     *
     * @param rental the rental object to be terminated
     * @throws DBException if failed to terminate the rental
     */
    public void studentTerminateRental(Rental rental) throws DBException {
        int rentalID = rental.getRentalID();
        int instrumentID = rental.getInstrumentID();

        String failureMessage = "Could not terminate rental: " + rentalID + ".";

        int updatedRows = 0;

        try {
            terminateRentalStmt.setInt(1, rentalID);

            updatedRows = terminateRentalStmt.executeUpdate();

            if (updatedRows != 1) {
                handleException(failureMessage, null);
            }
            updateInstrumentStatus(instrumentID, true);
            connection.commit();
        } catch (SQLException | DBException e) {
            handleException(failureMessage, e);
        }

    }

    private void connectToDB() throws SQLException {

        connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/school",
                "postgres", "example");

        connection.setAutoCommit(false);
    }

    private void prepareStatements() throws SQLException {

        findAllAvailableInstrumentsStmt = connection.prepareStatement("SELECT " + SCHOOL_INSTRUMENT_ID +
                ", type, brand, price_per_month" +
                " FROM " + AVAIL_INSTR_TABLE_NAME + " INNER JOIN "+ INSTR_TYPE_TABLE_NAME +
                " ON " + AVAIL_INSTR_TABLE_NAME + "."+INST_TYPE_ID +" = " + INSTR_TYPE_TABLE_NAME + "." + INST_TYPE_ID  +
                " WHERE " +AVAILABLE +" = 'TRUE' ORDER BY " + SCHOOL_INSTRUMENT_ID);

        findActiveRentalsWithStudent = connection.prepareStatement("SELECT " + RENTAL_ID + ", " +  STUDENT_PK + ", "
                + RENTAL_LEASE_START + ", " + RENTAL_LEASE_END + ", " + RENTAL_TABLE_NAME + "." +SCHOOL_INSTRUMENT_ID +
                " FROM " + RENTAL_TABLE_NAME + " JOIN " + AVAIL_INSTR_TABLE_NAME + " ON " +RENTAL_TABLE_NAME + "."+
                        SCHOOL_INSTRUMENT_ID + "=" + AVAIL_INSTR_TABLE_NAME + "." + SCHOOL_INSTRUMENT_ID +
                " WHERE " + STUDENT_PK + " = ? AND " + CURRENT_DATE + " BETWEEN " + RENTAL_LEASE_START + " AND " +
                  RENTAL_LEASE_END  + " AND " + AVAILABLE + " = false");

        countActiveStudentRental = connection.prepareStatement("SELECT COUNT(*) FROM " + RENTAL_TABLE_NAME +
                " JOIN " + AVAIL_INSTR_TABLE_NAME + " ON " +RENTAL_TABLE_NAME + "."+
                SCHOOL_INSTRUMENT_ID + "=" + AVAIL_INSTR_TABLE_NAME + "." + SCHOOL_INSTRUMENT_ID +
                        " WHERE " + STUDENT_PK + " = ? AND " + CURRENT_DATE + " BETWEEN " + RENTAL_LEASE_START + " AND " +
                        RENTAL_LEASE_END  + " AND " + AVAILABLE + " = false");

        studentRentStmt = connection.prepareStatement("INSERT INTO " + RENTAL_TABLE_NAME +
                " (\"" + STUDENT_PK + "\", \""+RENTAL_LEASE_START+"\", \""+ RENTAL_LEASE_END +"\", \"" + SCHOOL_INSTRUMENT_ID + "\") " +
                "VALUES (?, " + CURRENT_DATE + ", ?, ?)");

        findStudentStmt = connection.prepareStatement("SELECT "+ STUDENT_PK + " , first_name, last_name " +
                "FROM student WHERE " + STUDENT_PK + " = ?");

        updateInstrumentStatusStmt = connection.prepareStatement("UPDATE " + AVAIL_INSTR_TABLE_NAME + " SET \""+AVAILABLE+"\" = ?" +
                " WHERE \"" + SCHOOL_INSTRUMENT_ID + "\" = ?");

        terminateRentalStmt = connection.prepareStatement("UPDATE " + RENTAL_TABLE_NAME + " SET \"" + RENTAL_LEASE_END
                + "\" = " + CURRENT_DATE + " WHERE \""+RENTAL_ID+"\" = ?");

    }


    private void handleException(String message, Exception cause) throws DBException {
        String failureMsg = message;
        try {
            connection.rollback();
        } catch (SQLException sqlExc) {
            failureMsg = failureMsg + ". Rollback failed due to: " + sqlExc.getMessage();
        }

        if (cause != null) {
            throw new DBException(message, cause);
        } else {
            throw new DBException(message);
        }
    }

    private void closeResultSet(String message, ResultSet result) throws DBException {
        try {
            result.close();
        } catch (Exception e) {
            throw new DBException(message + ". Could not close result set.", e);
        }
    }
}
