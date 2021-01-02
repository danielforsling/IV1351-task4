package se.kth.iv1351.smschool.controller;

import se.kth.iv1351.smschool.integration.SMschoolDB;
import se.kth.iv1351.smschool.integration.DBException;
import se.kth.iv1351.smschool.model.Instrument;
import se.kth.iv1351.smschool.model.Rental;
import se.kth.iv1351.smschool.model.RetrieveDataException;
import se.kth.iv1351.smschool.model.Student;

import java.util.List;

public class Controller {

    private final SMschoolDB schoolDatabase;

    public Controller() throws DBException {
        this.schoolDatabase = new SMschoolDB();
    }

    public List<? extends Instrument> getAllAvailableInstruments()
    throws Exception{
        try {
            return schoolDatabase.listAllInstruments();
        } catch (DBException e) {
            throw new RetrieveDataException("Failed to retrieve instruments",e);
        }
    }

    public Student findStudent(int i) throws RetrieveDataException {
        try {
            return schoolDatabase.findStudent(i);
        } catch (DBException e) {
           throw new RetrieveDataException("Failed to retrieve student", e);
        }
    }

    public List getActiveRentalsForStudent(int studentID)
                                            throws RetrieveDataException{
        try {
            return schoolDatabase.listActiveRentalsForStudent(studentID);
        } catch (DBException e) {
           throw new RetrieveDataException("Failed to retrieve rentals", e);
        }
    }

    public int getNumberOfActiveRentals(int studentID)
                                                throws RetrieveDataException {
        try {
            return schoolDatabase.countStudentActiveRentals(studentID);
        } catch (DBException e) {
           throw new RetrieveDataException("Failed to retrieve number of rentals", e);
        }
    }
}
