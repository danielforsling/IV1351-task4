package se.kth.iv1351.smschool.model;


/**
 * @author Daniel Forsling 2020-12-31
 *
 *
 */
public class Instrument {

    private int id;
    private String type;
    private String brand;
    private int pricePerMonth;

    /**
     *  constructor
     *
     * @param id
     * @param type
     * @param brand
     * @param pricePerMonth
     */
    public Instrument(int id, String type, String brand, int pricePerMonth) {
        this.id = id;
        this.type = type;
        this.brand = brand;
        this.pricePerMonth = pricePerMonth;
    }

    /**
     * @return A string representation of this object.
     */
    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("instrument: [ id: " + id);
        sb.append(", type: " + type);
        sb.append(", brand: " + brand);
        sb.append(", price/month: " + pricePerMonth);
        sb.append("]");

        return sb.toString();
    }
}
