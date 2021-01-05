/*
 *    @author Daniel Forsling 2020-12-31
 */
package se.kth.iv1351.smschool.model;

/**
 *  This class represents an rental of an instrument by a student of a music school.
 */
public class Rental {

    private final int rentalID;
    private final int studentID;
    private final String leaseStart;
    private final String leaseEnd;
    private final int instrumentID;

    /**
     * Creates an instance of this class.
     *
     * @param rentalID The id of the rental.
     * @param studentID The id of the student that has rented.
     * @param leaseStart The start date of the rental.
     * @param leaseEnd The end date of the rental.
     * @param instrumentID The id of the instrument rented.
     */
    public Rental(int rentalID, int studentID, String leaseStart, String leaseEnd, int instrumentID) {
        this.rentalID = rentalID;
        this.studentID = studentID;
        this.leaseStart = leaseStart;
        this.leaseEnd = leaseEnd;
        this.instrumentID = instrumentID;
    }

    /**
     * @return The id of the rental.
     */
    public int getRentalID() {
        return rentalID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rental rental = (Rental) o;
        return rentalID == rental.rentalID &&
                studentID == rental.studentID;
    }

    /**
     * @return The instrument id of the rental.
     */
    public int getInstrumentID() {
        return instrumentID;
    }

    /**
     * Returns the Rental-object when receiving the id.
     * @param rentalID The id of the rental.
     * @return The Rental-object
     */
    public Rental getRental(int rentalID) {
        if (this.rentalID ==rentalID)
            return this;
        else return null;
    }

    /**
     * @return A string representation of this object.
     */
    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("Rental: [ rentalID: ").append(rentalID);
        sb.append(", studentID: ").append(studentID);
        sb.append(", lease start: ").append(leaseStart);
        sb.append(", lease end: ").append(leaseEnd);
        sb.append(", instrument ID: ").append(instrumentID);
        sb.append("]");

        return sb.toString();
    }
}
