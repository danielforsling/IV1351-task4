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

package se.kth.iv1351.smschool;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * A small program that illustrates how to write a simple JDBC program.
 */
public class BasicJdbc {

    private static final String TABLE_NAME = "person";
    private static final String RENTAL_COL1 = "rental_id";
    private static final String RENTAL_COL2 = "student_id";
    private static final String RENTAL_COL3 = "lease_start";
    private static final String RENTAL_COL4 = "lease_end";

    private static final String AVAIL_INSTR_COL1 = "school_instrument_id";
    private static final String AVAIL_INSTR_COL2 = "type";
    private static final String AVAIL_INSTR_COL3 = "brand";
    private static final String AVAIL_INSTR_COL4 = "price_per_month";

    private Connection connection;

    private PreparedStatement createPersonStmt;
    private PreparedStatement findAllPersonsStmt;
    private PreparedStatement deletePersonStmt;

    private PreparedStatement findAllRentals;
    private PreparedStatement findAllAvailableInstrumentsStmt;

    public static void main(String[] args) {
        new BasicJdbc().accessDB();
    }

    private void accessDB() {
        try {
            connectToDB();
            prepareStatements();

            listAllRentals();
            connection.commit();

            listAllInstruments();
            connection.commit();

        } catch (SQLException | ClassNotFoundException exc) {
            exc.printStackTrace();
        }
    }

    private void listAllRentals() {
        try {

            int counter = 0;

            ResultSet result = null;
            result = findAllRentals.executeQuery();

            if (!result.wasNull())
                System.out.println(RENTAL_COL1 + " " + RENTAL_COL2 + " " + RENTAL_COL3 + " " + RENTAL_COL4 + " " + RENTAL_COL4);
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

    private void listAllInstruments() {
        try {
            int counter = 0;

            ResultSet result = null;
            result = findAllAvailableInstrumentsStmt.executeQuery();

            if (!result.wasNull())
                System.out.println(AVAIL_INSTR_COL1 + " " + AVAIL_INSTR_COL2 + " " + AVAIL_INSTR_COL3 + " " + AVAIL_INSTR_COL4);
            while (result.next()) {
                System.out.println(result.getString(1) + " " +
                        result.getString(2) + " " +
                        result.getString(3) + " " +
                        result.getString(4));
                counter++;
            }
            System.out.println(counter + " results.");

        } catch (SQLException sqlErr) {
            sqlErr.printStackTrace();
        }
    }

    private void connectToDB() throws SQLException, ClassNotFoundException {
        // Class.forName("org.postgresql.Driver");

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
        createPersonStmt = connection.prepareStatement("INSERT INTO " + TABLE_NAME + " VALUES (?, ?, ?)");
        findAllPersonsStmt = connection.prepareStatement("SELECT * from " + TABLE_NAME);
        deletePersonStmt = connection.prepareStatement("DELETE FROM " + TABLE_NAME + " WHERE name = ?");

        findAllAvailableInstrumentsStmt = connection.prepareStatement("SELECT school_instrument_id, type, brand, price_per_month" +
                " FROM available_instruments INNER JOIN instrument_type" +
                " ON available_instruments.instrument_type_id = instrument_type.instrument_type_id " +
                "WHERE available = 'TRUE'");

        findAllRentals = connection.prepareStatement("SELECT * FROM rental");
    }
}
