package se.kth.iv1351.smschool.model;

public class Rental {

    private int rentalID;
    private int studentID;
    private String leaseStart;
    private String leaseEnd;
    private int instrumentID;

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

    public int getInstrumentID() {
        return instrumentID;
    }

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
        sb.append("Rental: [ rentalID: " + rentalID);
        sb.append(", studentID: " + studentID);
        sb.append(", lease start: " + leaseStart);
        sb.append(", lease end: " + leaseEnd);
        sb.append(", instrument ID: " + instrumentID);
        sb.append("]");

        return sb.toString();
    }
}
