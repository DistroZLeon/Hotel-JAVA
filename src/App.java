import java.io.*;
import java.util.Scanner;

public class App {
    @SuppressWarnings({"ConvertToTryWithResources", "UseSpecificCatch"})
    public static void main(String[] args) throws Exception {
        Service service= Service.getInstance();
        Scanner firstInteraction= new Scanner(System.in);
        Scanner readScanner = null, hotelScanner= null;
        File fileService;
        boolean goodFile;
        goodFile= false;
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
        if(readMethod.toLowerCase().equals("console"))
            readScanner= new Scanner(System.in);
        else{
            goodFile= false;
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
            System.out.println("Welcome to the interface related to Guest Grpups");
            System.out.println("Choose the action you want to make:\n Make a new Reservation (Press 0)\nCancel an already made Reservation (Press 1)\nView the details for an already made Reservation (Press 2)\nView the details of all the valid and not cancelled Reservations (Press 3)\nExit Interface (Press 4)");
            int choiceStep= readScanner.nextInt();
            switch (choiceStep) {
                case 0 ->  {
                    int index= service.readGuestGroupDetails(readScanner);
                    service.makeReservation(index);
                    System.out.println(service.getGroup(index));
                    break;
                }
                case 1 ->  {
                    System.out.println("Enter the id of the GuestGroup.");
                    int index= readScanner.nextInt();
                    service.cancelReservation(index);
                    break;
                }
                case 2 -> {
                    System.out.println("Enter the id of the GuestGroup.");
                    int index= readScanner.nextInt();
                    System.out.println(service.getGroup(index));
                    break;
                }
                case 3 ->  {
                    int index= service.getIndex();
                    for(int i=1; i< index; ++i)
                        if(service.getGroup(i).getState()==1)
                            System.out.println(service.getGroup(i));
                    break;
                }
                case 4 ->  {
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
