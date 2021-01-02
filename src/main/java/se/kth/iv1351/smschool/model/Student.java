package se.kth.iv1351.smschool.model;

public class Student {

    private int studentID;
    private String firstName;
    private String lastName;

    private int noOfRentals = 0;

    public Student(int studentID, String firstName, String lastName) {
        this.studentID = studentID;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public int getNoOfRentals() {
        return noOfRentals;
    }

    public void setNoOfRentals(int noOfRentals) {
        this.noOfRentals = noOfRentals;
    }

    public int getStudentID() {
        return studentID;
    }

    /**
     * @return A string representation of this object.
     */
    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append(firstName + " ");
        sb.append(lastName + ", ");
        sb.append("id: " + studentID);

        return sb.toString();
    }
}
