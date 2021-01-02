package se.kth.iv1351.smschool.model;

public class Rental {

    private int rentalID;
    private int studentID;
    private String leaseStart;
    private String leaseEnd;
    private int instrumentID;

    public int getRentalID() {
        return rentalID;
    }

    public int getInstrumentID() {
        return instrumentID;
    }

    /**
     *  constructor
     */
    public Rental(int rentalID, int studentID, String leaseStart, String leaseEnd, int instrumentID) {
        this.rentalID = rentalID;
        this.studentID = studentID;
        this.leaseStart = leaseStart;
        this.leaseEnd = leaseEnd;
        this.instrumentID = instrumentID;
    }

    /**
     * @return A string representation of this object.
     */
    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("Rental: [ id: " + rentalID);
        sb.append(", studentID: " + studentID);
        sb.append(", lease start: " + leaseStart);
        sb.append(", lease end: " + leaseEnd);
        sb.append(", instrument ID: " + instrumentID);
        sb.append("]");

        return sb.toString();
    }
}
