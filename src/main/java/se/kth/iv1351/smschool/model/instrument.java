/*
 *    @author Daniel Forsling 2020-12-31
 */
package se.kth.iv1351.smschool.model;

/**
 *  This class represents an instrument rented by a student of a music school.
 */
public class Instrument {

    private final int id;
    private final String type;
    private final String brand;
    private final int pricePerMonth;

    /**
     *  Creates an instance of this class.
     *
     * @param id The id of the instrument.
     * @param type The type of the instrument.
     * @param brand The brand of the instrument.
     * @param pricePerMonth The renting price/month for this instrument.
     */
    public Instrument(int id, String type, String brand, int pricePerMonth) {
        this.id = id;
        this.type = type;
        this.brand = brand;
        this.pricePerMonth = pricePerMonth;
    }

    /**
     * @return The id of the instrument.
     */
    public int getId() {
        return id;
    }

    /**
     * @return A string representation of this object.
     */
    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("instrument: [ id: ");
        sb.append(id);
        sb.append(", type: ");
        sb.append(type);
        sb.append(", brand: ");
        sb.append(brand);
        sb.append(", price/month: ");
        sb.append(pricePerMonth);
        sb.append("]");

        return sb.toString();
    }
}
