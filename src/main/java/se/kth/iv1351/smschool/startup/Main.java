package se.kth.iv1351.smschool.startup;

import se.kth.iv1351.smschool.controller.Controller;
import se.kth.iv1351.smschool.integration.DBException;
import se.kth.iv1351.smschool.model.Instrument;
import se.kth.iv1351.smschool.model.Rental;
import se.kth.iv1351.smschool.model.RetrieveDataException;
import se.kth.iv1351.smschool.model.Student;

import java.sql.SQLOutput;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static <Rentals> void main(String[] args) {
        try {
            Controller contr = new Controller();

            Scanner input = new Scanner(System.in);


        /*
            listAllRentals();
            connection.commit();
*/
            //          listAllInstruments();
            //          connection.commit();

            //   listActiveRentalsForStudent();
            //   connection.commit();
            System.out.println("starting");
    /*
            List<? extends Instrument> list = contr.getAllAvailableInstruments();
            for (Instrument instr: list) {
                System.out.println(instr);
            }
     */
            int i;
            while (true) {
                System.out.println("enter your id to log in: ");
                i = input.nextInt();
                if (i == 0)
                    break;
                Student loggedIn = contr.findStudent(i);
                if(loggedIn == null) {
                    System.out.println("Enter a valid id!");
                }
                while(i != 0 && loggedIn!=null) {
                    System.out.println("Logged in as: " + loggedIn.getFirstName() + " " + loggedIn.getLastName());
                    System.out.println("1 - check rentals");
                    System.out.println("0 - log out");
                   //move move
                    loggedIn.setNoOfRentals(contr.getNumberOfActiveRentals(loggedIn.getStudentID()));
                    System.out.println("no of rentals: " +  loggedIn.getNoOfRentals());
                    i = input.nextInt();
                    switch (i) {
                        case 1:
                           List<Rental> list = contr.getActiveRentalsForStudent(loggedIn.getStudentID());
                           if(list.isEmpty())
                               System.out.println("No rentals");
                           else {
                               for (Rental rent : list) {
                                   System.out.println(rent);
                               }
                           }

                        default:
                            break;
                    }


                }
            }
/*

                boolean bool =  db.studentCanRent(i);
                db.connection.commit();
                if(bool) {
                    System.out.println(i + ": can rent" );
                    System.out.println("enter instr id");
                    int instr = input.nextInt();
                    if(db.instrumentIsAvailable(instr)) {
                        System.out.println("instrument available. Do you want to rent? yes - [1]");
                        if (input.nextInt() == 1) {
                            System.out.println("You can rent an instrument at maximum 12 month.\n " +
                                    "Please enter how many month you want to rent: ");
                            int month = input.nextInt();
                            if (month > 12 || month < 1) {
                                System.out.println("Invalid input. Cannot rent instrument in " + month + " month(s)");
                            }

                            Date dt = new Date();


                            LocalDate returnDate = LocalDate.now().plusMonths(month);
                            if(returnDate.isAfter(LocalDate.now()))
                                db.studentRentInstrument(i, returnDate.toString(), instr );

                        }
                    } else  {
                        System.out.println("instrument not available");
                    }
                } else {
                    System.out.println(i + ": cannot rent" );
                }
            }
*/

        } catch (DBException | RetrieveDataException e) {
            System.out.println("Error somehow");
            e.printStackTrace();
        }


    }
}
