import java.beans.Statement;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Scanner;

public class App {
    @SuppressWarnings({"ConvertToTryWithResources", "UseSpecificCatch"})
    public static void main(String[] args) throws Exception {
        String url = "jdbc:postgresql://localhost:5432/hoteldb?user=postgres&password=proiectjava";
        try (Connection conn = DriverManager.getConnection(url)) {
            System.out.println("Connected to PostgreSQL!");
            
            // Example query
            Statement stmt = (Statement) conn.createStatement();
            ResultSet rs = ((java.sql.Statement) stmt).executeQuery("SELECT version()");
            if (rs.next()) {
                System.out.println("PostgreSQL Version: " + rs.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Service service= Service.getInstance();
        Scanner firstInteraction= new Scanner(System.in);
        Scanner readScanner = null, hotelScanner= null;
        File fileService;
        boolean goodFile;
        goodFile= false;
        // Choosing the right file to read the hotel's details
        while(!goodFile){
            System.out.println("Enter the source file for the hotel's data");
            String fileHotelString= firstInteraction.nextLine();
            fileHotelString= fileHotelString.trim();
            try {
                fileService= new File("C:/Facultate/OOPA-Java/Project/lib/" +fileHotelString);
                hotelScanner= new Scanner(fileService);
                goodFile= true;
            }
            catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        service.readHotelDetails(hotelScanner);
        System.out.println("Choose the input method: File or Console");
        String readMethod= firstInteraction.nextLine();
        readMethod=readMethod.trim();
        //Chosing either to read from the console or from a file to read for the GuestGroup interface
        if(readMethod.toLowerCase().equals("console"))
            readScanner= new Scanner(System.in);
        else{
            goodFile= false;
            // Getting the right file to read from for the GuestGroup Interface
            while(!goodFile){
                System.out.println("Enter the source file for all the data");
                String fileServiceString= firstInteraction.nextLine();
                fileServiceString= fileServiceString.trim();
                try {
                    fileService= new File("C:/Facultate/OOPA-Java/Project/lib/" +fileServiceString);
                    readScanner= new Scanner(fileService);
                    goodFile= true;
                }
                catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }
        }
        boolean continueWithGuestsGroups=true;
        while(continueWithGuestsGroups){
            System.out.println("Welcome to the interface related to Guest Groups");
            System.out.println("Choose the action you want to make:\nMake a new Reservation (Press 0)\nCancel an already made Reservation (Press 1)\nView the details for an already made Reservation (Press 2)\nView the details of all the valid and not cancelled Reservations (Press 3)\nShow the total price for a certain group (Press 4)\nView all the groups sorted either ascending or descending, having the sorting criteria the total cost (Press 5)\nExit Interface (Press 6)\n");
            int choiceStep= readScanner.nextInt();
            switch (choiceStep) {
                case 0 ->  {
                    System.out.println("Do you want a specific interval or the earliest date? If you want to choose a specific one, enter any positive number, or any negative number or 0 otherwise.\nKeep in mind that the interval might not be available.");
                    int choice= readScanner.nextInt(), theMinDay=1;
                    if(choice>0){
                        System.out.println("Enter the starting date: ");
                        theMinDay= readScanner.nextInt(); 
                    }
                    int index= service.readGuestGroupDetails(readScanner);
                    service.makeReservation(index, theMinDay);
                    GuestGroup group =service.getGroup(index);
                    if(group!=null)
                        System.out.println(group);
                    break;
                }
                case 1 ->  {
                    System.out.println("Enter the id of the GuestGroup.");
                    int index= readScanner.nextInt();
                    GuestGroup group= service.getGroup(index);
                    if(group==null)
                    System.out.println("The index introduced is not a valid one!");
                    else
                        service.cancelReservation(index);
                    break;
                }
                case 2 -> {
                    System.out.println("Enter the id of the GuestGroup.");
                    int index= readScanner.nextInt();
                    readScanner.nextLine();
                    GuestGroup group= service.getGroup(index);
                    if(group==null)
                        System.out.println("The index introduced is not a valid one!");
                    else{
                        System.out.println("-------------------------------------------------------------");
                        System.out.println(group);
                        System.out.println("-------------------------------------------------------------");
                    }
                    break;
                }
                case 3 ->  {
                    System.out.println("-------------------------------------------------------------");
                    for(Integer id: service.getAllGroups().keySet())
                        System.out.println(service.getGroup(id));
                    System.out.println("-------------------------------------------------------------");
                    break;
                }
                case 4 ->{
                    System.out.println("Enter the id of the GuestGroup.");
                    int index= readScanner.nextInt();
                    readScanner.nextLine();
                    double cost= service.totalPriceForGroup(index);
                    if(cost== -1)
                        System.out.println("The Id that you've entered, "+ index+ " is not a valid id.");
                    else
                    System.out.println("The total cost for the group with the Id "+ index+ " is "+ cost+ "\n");
                }
                case 5 ->{
                    System.out.println("Enter either a negative number, for descending sort, or a positive number, for a ascending sort!");
                    int order= readScanner.nextInt();
                    readScanner.nextLine();
                    Map<Integer, GuestGroup> orderedList= service.sortedByTotalPrice(order);
                    System.out.println("\n-------------------------------------------------------------");
                    for (Map.Entry<Integer, GuestGroup> entry : orderedList.entrySet()) {
                        GuestGroup group = entry.getValue();
                        System.out.println(group);
                        System.out.println("Cost: " + service.totalPriceForGroup(group.getId()) + "\n");
                    }
                    System.out.println("-------------------------------------------------------------\n");
                }
                case 6 ->  {
                    continueWithGuestsGroups= false;
                    System.out.println("We hope you had a good experience with our app!\nHave a good day!");
                    break;
                }
                default -> {
                    System.out.println("You've entered an invalid option.");
                    break;
                }
            }
        }
        firstInteraction.close();
        if(readScanner!= null)
            readScanner.close();
        if(hotelScanner!=null)
            hotelScanner.close();
    }
}
