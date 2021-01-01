package se.kth.iv1351.smschool.controller;

import se.kth.iv1351.smschool.integration.SMschoolDB;
import se.kth.iv1351.smschool.integration.DBException;
import se.kth.iv1351.smschool.model.Instrument;
import se.kth.iv1351.smschool.model.RetrieveDataException;

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
}
