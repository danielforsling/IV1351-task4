/*
 *    @author Daniel Forsling 2020-12-31
 */
package se.kth.iv1351.smschool.model;

/**
 * This class represent a student of a music school.
 */
public class Student {

    private final int studentID;
    private final String firstName;
    private final String lastName;
    private int noOfRentals = 0;


    /**
     * Creates an instance of this class.
     *
     * @param studentID The id of the student.
     * @param firstName The first name of the student.
     * @param lastName The last name of the student.
     */
    public Student(int studentID, String firstName, String lastName) {
        this.studentID = studentID;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    /**
     * @return The last name of the student
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @return The first name of the student.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @return returns the number of rentals the student has.
     */
    public int getNoOfRentals() {
        return noOfRentals;
    }

    /**
     * Sets the number of rentals the student has.
     * @param noOfRentals The new number of rentals.
     */
    public void setNoOfRentals(int noOfRentals) {
        this.noOfRentals = noOfRentals;
    }

    /**
     * @return returns the id of the student.
     */
    public int getStudentID() {
        return studentID;
    }

    /**
     * @return A string representation of this object.
     */
    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append(firstName).append(" ");
        sb.append(lastName).append(", ");
        sb.append("id: ").append(studentID);

        return sb.toString();
    }
}
