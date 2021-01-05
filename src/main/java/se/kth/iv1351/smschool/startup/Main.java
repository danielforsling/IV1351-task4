package se.kth.iv1351.smschool.startup;

import se.kth.iv1351.smschool.controller.Controller;
import se.kth.iv1351.smschool.integration.DBException;
import se.kth.iv1351.smschool.model.Instrument;
import se.kth.iv1351.smschool.model.Rental;
import se.kth.iv1351.smschool.model.RetrieveDataException;
import se.kth.iv1351.smschool.model.Student;

import java.sql.SQLOutput;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        try {
            Controller contr = new Controller();

            Scanner input = new Scanner(System.in);

            System.out.println("starting");
            int i;
            while (true) {
                System.out.println("enter your id to log in: ");
                i = input.nextInt();
                if (i == 0)
                    break;
                Student loggedIn = contr.findStudent(i);
                if (loggedIn == null) {
                    System.out.println("Enter a valid id!");
                }
                while (i != 0 && loggedIn != null) {
                    System.out.println("Logged in as: " + loggedIn.getFirstName() + " " + loggedIn.getLastName());
                    System.out.println("1 - check available instruments");
                    System.out.println("2 - check rentals");
                    System.out.println("3 - rent an instrument");
                    System.out.println("4 - terminate rental");
                    System.out.println("0 - log out\n");

                    i = input.nextInt();
                    switch (i) {
                        case 1:
                            getAvailableInstruments(contr);
                            pressEnter(input);
                            break;
                        case 2:
                            getRentals(loggedIn, contr);
                            pressEnter(input);
                            break;
                        case 3:
                            rentInstrument(loggedIn, input, contr);

                            pressEnter(input);
                            break;
                        case 4:
                            terminateRental(loggedIn, contr, input);
                            pressEnter(input);
                            break;
                        default:
                            break;
                    }

                }
            }

        } catch (DBException | RetrieveDataException e) {
            System.out.println("Error somehow");
            e.printStackTrace();
        }

    }

    private static void getAvailableInstruments(Controller controller) throws RetrieveDataException {
        System.out.println("All available instruments:");
        List<? extends Instrument> list = controller.getAllAvailableInstruments();
        for (Instrument instr : list) {
            System.out.println(instr);
        }

    }

    private static List getRentals(Student student, Controller controller) throws RetrieveDataException {
        System.out.println("Your active rentals:");
        List<Rental> list = controller.getActiveRentalsForStudent(student.getStudentID());
        if (list.isEmpty()) {
            System.out.println("You have no rentals");
        } else {
            for (Rental rent : list) {
                System.out.println(rent);
            }

        }
        student.setNoOfRentals(controller.getNumberOfActiveRentals(student.getStudentID()));
        System.out.println("Number of rentals: " + student.getNoOfRentals() + "\n");
        return list;
    }

    private static void rentInstrument(Student loggedIn, Scanner input, Controller controller) throws RetrieveDataException {

        int noOfRentals = loggedIn.getNoOfRentals();
        if (noOfRentals < 2) {
            System.out.println("You can rent " + (2 - noOfRentals) + " instruments");
            System.out.println("Enter the id of the instrument you want to rent");
            int instr = input.nextInt();
            if (controller.instrumentIsAvailable(instr)) {
                System.out.println("Instrument is available.");

                System.out.println("You can rent an instrument at maximum 12 months.\n " +
                        "Please enter how many month(s) you want to rent: ");
                int month = input.nextInt();
                if (month > 12 || month < 1) {
                    System.out.println("Invalid input. Cannot rent instrument in " + month + " month(s)");
                } else {
                    LocalDate returnDate = LocalDate.now().plusMonths(month);
                    if (returnDate.isAfter(LocalDate.now())) {

                        System.out.println("Confirm that you want to rent instrument with id: " + instr +
                                " and return date: " + returnDate + "\n To confirm, enter [1]");
                        if (input.nextInt() == 1) {
                            controller.studentRentInstrument(loggedIn.getStudentID(), returnDate.toString(), instr);
                            System.out.println("You have now rented instrument with id: " + instr +
                                    " and return date: " + returnDate);
                        } else
                            System.out.println("Rental aborted");
                    } else
                        System.out.println("Invalid input");
                }
            } else {
                System.out.println("instrument not available");
            }
        } else {
            System.out.println("You cannot rent since you have " + loggedIn.getNoOfRentals() + " rentals.");
        }

    }

    private static void terminateRental(Student student, Controller controller, Scanner input) throws RetrieveDataException {
        List<Rental> list = getRentals(student, controller);
        System.out.println("Which rental do you want to terminate?\nEnter rentalID:");
        Rental rentalToTerminate = null;
        int index = input.nextInt();
        for (Rental rental: list) {
            if(rental.getRentalID()==index) {
                rentalToTerminate = rental.getRental(index);
                break;
            }
        }
        if (!list.contains(rentalToTerminate))
            System.out.println("Invalid input. No rental with id:" + index);
        else {
            try {
                System.out.println("Confirm that you want to terminate rental with id: " + rentalToTerminate.getRentalID() +
                        "\n and to return the instrument with id:" + rentalToTerminate.getInstrumentID() + " today \n To confirm, enter [1]");
                if (input.nextInt() == 1) {
                    controller.studentTerminateRental(rentalToTerminate);
                    System.out.println("You have now terminated rental with id: " + rentalToTerminate.getInstrumentID());
                } else
                    System.out.println("Rental aborted");

            } catch (RetrieveDataException e) {
                e.printStackTrace();
            }
        }


    }


    private static void pressEnter(Scanner input) {
        System.out.println("Press enter to continue");
        input.nextLine();
        input.nextLine();
        System.out.println();
    }
}
