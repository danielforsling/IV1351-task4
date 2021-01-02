/*
 * The MIT License (MIT)
 * Copyright (c) 2020 Leif Lindbäck
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction,including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so,subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package se.kth.iv1351.smschool.integration;

import se.kth.iv1351.smschool.model.Instrument;
import se.kth.iv1351.smschool.model.Rental;
import se.kth.iv1351.smschool.model.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


/**
 * A small program that illustrates how to write a simple JDBC program.
 */
public class SMschoolDB {

    private static final String TABLE_NAME = "person";
    private static final String RENTAL = "rental";
    private static final String RENTAL_COL1 = "rental_id";
    private static final String RENTAL_COL2 = "student_id";
    private static final String RENTAL_COL3 = "lease_start";
    private static final String RENTAL_COL4 = "lease_end";

    private static final String AVAIL_INSTR_COL1 = "school_instrument_id";
    private static final String AVAIL_INSTR_COL2 = "type";
    private static final String AVAIL_INSTR_COL3 = "brand";
    private static final String AVAIL_INSTR_COL4 = "price_per_month";

    private static final String STUDENT_PK = "student_id";

    private Connection connection;

    private PreparedStatement createPersonStmt;
    private PreparedStatement findAllPersonsStmt;
    private PreparedStatement deletePersonStmt;

    private PreparedStatement findActiveRentalsWithStudent;

    private PreparedStatement findAllRentals;
    private PreparedStatement findAllAvailableInstrumentsStmt;


    private PreparedStatement countActiveStudentRental;
    private PreparedStatement studentRentStmt;
    private PreparedStatement findStudentStmt;

    public SMschoolDB() throws DBException {
        try {
            connectToDB();
            prepareStatements();

        } catch (SQLException | ClassNotFoundException exc) {
            throw new DBException("Could not connect to database.", exc);
        }
    }

    public Student findStudent(int studentID) throws DBException {
        String failureMessage = "Could not search for student " + studentID;

        ResultSet result = null;

        Student student = null;

        try {
            findStudentStmt.setInt(1, studentID);
            result = findStudentStmt.executeQuery();

            if (result.next()) {
                student = new Student(
                        (int)result.getInt(STUDENT_PK),
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

    private void listAllRentals() {

        try {
            int counter = 0;

            ResultSet result = null;
            result = findAllRentals.executeQuery();

            if (!result.wasNull())
                System.out.println(RENTAL_COL1 + " " + RENTAL_COL2 + " " + RENTAL_COL3 + " " + RENTAL_COL4 + " " + RENTAL_COL4);
            else {
                System.out.println("null?!?!?");
            }
            while (result.next()) {
                System.out.println(result.getString(1) + " " +
                        result.getString(2) + " " +
                        result.getString(3) + " " +
                        result.getString(4) + " " +
                        result.getString(5) + " ");
                counter++;
            }
            System.out.println(counter + " results.");

        } catch (SQLException sqlErr) {
            sqlErr.printStackTrace();
        }
    }

    public List<Instrument> listAllInstruments() throws DBException {
        String failureMessage = "Could not search for available instruments";


        List<Instrument> instruments = new ArrayList<>();

        try (ResultSet result = findAllAvailableInstrumentsStmt.executeQuery();) {
            System.out.println("inside try");
            System.out.println(result.toString());

            System.out.println(result.toString());
            int counter = 0;
            while (result.next()) {
                Instrument instr = new Instrument(result.getInt(1),
                        result.getString(2),
                        result.getString(3),
                        result.getInt(4));

                instruments.add(instr);
                counter++;
                System.out.println(counter);
            }
            System.out.println("after try");
            connection.commit();
        } catch (SQLException sqlE) {
            handleException(failureMessage, sqlE);
        } /*
         finally {
            System.out.println(result.toString());
            closeResultSet(failureMessage, result);
        }

        */
        return instruments;
    }

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

    public int countStudentActiveRentals(int studentID) throws DBException {
        String failureMessage = "Could not search for rentals of specified student " + studentID + ".";
        ResultSet result = null;
        int activeRentals = 0;
        try {
            int counter = 0;

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


    private boolean studentCanRent(int studentID) throws DBException {
        int noOfRentals = countStudentActiveRentals(studentID);
        if (noOfRentals < 2)
            return true;
        else
            return false;
    }

    private boolean instrumentIsAvailable(int instrumentID) throws DBException {
        List<Instrument> list = listAllInstruments();
        for (Instrument instr : list) {
            if (instr.getId() == instrumentID)
                return true;
        }
        return false;
    }

    private void studentRentInstrument(int studentID, String leaseEnd, int instrumentID) throws DBException {
        String failureMessage = "Could not rent instrument to specified student " + studentID + ".";

        System.out.println("id: " + studentID + " leaseEnd: " + leaseEnd + " in_id: " + instrumentID);

        System.out.println("INSERT INTO " + RENTAL +
                " (\"" + STUDENT_PK + "\", \"lease_start\", \"lease_end\", \"school_instrument_id\") " +
                "VALUES (?, CURRENT_DATE, (CURRENT_DATE + interval '?' month)::date, ?)");

        int updatedRows = 0;

        try {
            //  if (findStudent(studentID) != studentID)
            //      return;

            studentRentStmt.setInt(1, studentID);
            studentRentStmt.setDate(2, java.sql.Date.valueOf(leaseEnd));
            studentRentStmt.setInt(3, instrumentID);

            // studentRentStmt.setDate(1,2);
            String s = studentRentStmt.toString();

            System.out.println(studentRentStmt.toString());


            updatedRows = studentRentStmt.executeUpdate();

            if (updatedRows != 1) {
                handleException(failureMessage, null);
            }
            connection.commit();

        } catch (SQLException | DBException sqlE) {
            handleException(failureMessage, sqlE);
        }
    }

    private void connectToDB() throws SQLException, ClassNotFoundException {
        //    Class.forName("org.postgresql.Driver");

        connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/school",
                "postgres", "example");

        connection.setAutoCommit(false);
    }

    private boolean tableExists(Connection connection) throws SQLException {
        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet tableMetaData = metaData.getTables(null, null, null, null);
        while (tableMetaData.next()) {
            String tableName = tableMetaData.getString(3);
            if (tableName.equalsIgnoreCase(TABLE_NAME)) {
                return true;
            }
        }
        return false;
    }

    private void listAllRows() {
        try (ResultSet persons = findAllPersonsStmt.executeQuery()) {
            while (persons.next()) {
                System.out.println(
                        "name: " + persons.getString(1) + ", phone: " + persons.getString(2) + ", age: " + persons.getInt(3));
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }

    private void prepareStatements() throws SQLException {
        //     createPersonStmt = connection.prepareStatement("INSERT INTO " + TABLE_NAME + " VALUES (?, ?, ?)");
        //     findAllPersonsStmt = connection.prepareStatement("SELECT * from " + TABLE_NAME);
//        deletePersonStmt = connection.prepareStatement("DELETE FROM " + TABLE_NAME + " WHERE name = ?");

        findAllAvailableInstrumentsStmt = connection.prepareStatement("SELECT school_instrument_id, type, brand, price_per_month" +
                " FROM available_instruments INNER JOIN instrument_type" +
                " ON available_instruments.instrument_type_id = instrument_type.instrument_type_id " +
                "WHERE available = 'TRUE'");

        findAllRentals = connection.prepareStatement("SELECT * FROM " + RENTAL);
        findActiveRentalsWithStudent = connection.prepareStatement("SELECT * FROM " + RENTAL +
                " WHERE student_id = ? AND CURRENT_DATE BETWEEN lease_start AND lease_end");

        countActiveStudentRental = connection.prepareStatement("SELECT COUNT(*) FROM " + RENTAL +
                " WHERE student_id = ? AND CURRENT_DATE BETWEEN lease_start AND lease_end");

        studentRentStmt = connection.prepareStatement("INSERT INTO " + RENTAL +
                " (\"" + STUDENT_PK + "\", \"lease_start\", \"lease_end\", \"school_instrument_id\") " +
                "VALUES (?, CURRENT_DATE, ?, ?)");

        findStudentStmt = connection.prepareStatement("SELECT student_id, first_name, last_name FROM student WHERE " +
                STUDENT_PK + " = ?");

    }

    /*

    SELECT (CURRENT_DATE + interval '12 month')::date

    INSERT INTO "rental" ("student_id", "lease_start", "lease_end", "school_instrument_id")
VALUES ('1', now(), '2022-01-01', '32');

INSERT INTO rental
("student_id", "lease_start", "lease_end", "school_instrument_id")
VALUES (1, CURRENT_DATE, (CURRENT_DATE + interval '5' month)::date, 32);

     */


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

/*
    public static void main(String[] args) throws SQLException, DBException {

    }
*/
}
