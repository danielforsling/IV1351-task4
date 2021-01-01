package se.kth.iv1351.smschool.startup;

import se.kth.iv1351.smschool.controller.Controller;
import se.kth.iv1351.smschool.integration.DBException;
import se.kth.iv1351.smschool.model.Instrument;

import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        try {
           Controller contr =  new Controller();

            Scanner input = new Scanner(System.in);

         //   SMschoolDB db =new SMschoolDB();
         //   db.accessDB();

        /*
            listAllRentals();
            connection.commit();
*/
            //          listAllInstruments();
            //          connection.commit();

            //   listActiveRentalsForStudent();
            //   connection.commit();
            System.out.println("starting");
            List<? extends Instrument> list = contr.getAllAvailableInstruments();
            for (Instrument instr: list) {
                System.out.println(instr);
            }
        /*
            int i;
            while(true) {
                System.out.println("enter your id: ");
                i = input.nextInt();
                if(i == 0)
                    break;

                if (db.findStudent(i) == i) {
                    System.out.println("you exists. congrats");
                }
                else {
                    System.out.println("enter valid id");
                    continue;
                }
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

        } catch (DBException e) {
            System.out.println("Error somehow");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
