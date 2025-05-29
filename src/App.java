import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

public class App {
    @SuppressWarnings({"ConvertToTryWithResources", "UseSpecificCatch"})
    public static void main(String[] args) throws Exception {
        HotelDAO hotelDAO= HotelDAO.getInstance();
        AuditService audit= AuditService.getInstance();
        Service service= Service.getInstance();
        Scanner firstInteraction= new Scanner(System.in);
        Scanner readScanner = null, hotelScanner= null;
        File fileService = null;
        boolean goodFile= false;
        HotelConfig config;
        // Choosing the right file to read the hotel's details
        while(!goodFile){
            System.out.println("Enter the source file for the hotel's data");
            String fileHotelString= firstInteraction.nextLine();
            fileHotelString= fileHotelString.trim();
            try {
                fileService= new File("C:/Facultate/OOPA-Java/Project/files/" +fileHotelString);
                hotelScanner= new Scanner(fileService);
                goodFile= true;
            }
            catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        service.readHotelDetails(hotelScanner);
        ArrayList<ArrayList<Integer>> roomDays= new ArrayList<>();
        Hotel hotel= Hotel.getInstance();
        for(int i=0; i<hotel.getNrApartments()+hotel.getNrNormals()+hotel.getNrIndividuals()+hotel.getNrScenes()+1; ++i){
            ArrayList<Integer> days= new ArrayList<>();
            if(i== hotel.getNrApartments()+hotel.getNrNormals()+hotel.getNrIndividuals()+hotel.getNrScenes())
                for(int j=1; j<366; ++j)
                   days.add(hotel.getRestaurant().getNrOfGuestsThatDay(j));
            else
                for(int j=1; j<366; ++j)
                    days.add(hotel.getRoom(i).getIdDays(j));                           
            roomDays.add(days);
        }
        config= new HotelConfig(fileService, roomDays);
        config= hotelDAO.read(config.getHashStructure());
        if(config==null){
            config= new HotelConfig(fileService, roomDays);
            int id= hotelDAO.create(config);
            config.setId(id);
            audit.log("New_Hotel");
        }
        else{
            for(int i=0; i<hotel.getNrApartments()+hotel.getNrNormals()+hotel.getNrIndividuals()+hotel.getNrScenes()+1; ++i){
                ArrayList<Integer> configurationList= config.getRoomDays().get(i);
                if(i== hotel.getNrApartments()+hotel.getNrNormals()+hotel.getNrIndividuals()+hotel.getNrScenes())
                    for(int j=1; j<366; ++j)
                        hotel.getRestaurant().setIdDays(configurationList.get(j-1), j, 1);
                else
                    for(int j=1; j<366; ++j)
                        hotel.getRoom(i).setIdDays(configurationList.get(j-1), j, 1);
            }
            audit.log("Using_Existing_Hotel");
        }
        service.setupGroups(config.getId());
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
                    fileService= new File("C:/Facultate/OOPA-Java/Project/files/" +fileServiceString);
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
            System.out.println("Choose the action you want to make:\nMake a new Reservation (Press 0)\nCancel an already made Reservation (Press 1)\nView the details for an already made Reservation (Press 2)\nView the details of all the valid and not cancelled Reservations (Press 3)\nShow the total price for a certain group (Press 4)\nView all the groups sorted either ascending or descending, having the sorting criteria the total cost (Press 5)\nShow the total winnings in a specific month (Press 6)\nShow the percentage of occupation for a certain room during a certain month (Press 7)\nCleanup the audit (Press 8)\nExit Interface (Press 9)\n");
            int choiceStep= readScanner.nextInt();
            readScanner.nextLine();
            switch (choiceStep) {
                case 0 ->  {
                    System.out.println("Do you want a specific interval or the earliest date? If you want to choose a specific one, enter any positive number, or any negative number or 0 otherwise.\nKeep in mind that the interval might not be available.");
                    int choice= readScanner.nextInt(), theMinDay=1;
                    if(choice>0){
                        System.out.println("Enter the starting date: ");
                        theMinDay= readScanner.nextInt(); 
                    }
                    int index= service.readGuestGroupDetails(readScanner);
                    index= service.makeReservation(index, theMinDay, config.getId());
                    GuestGroup group =service.getGroup(index);
                    if(group!=null){
                        System.out.println(group);
                        audit.log("New_Reservation");
                    }
                    else
                        audit.log("FAILED_New_Reservation");
                    break;
                }
                case 1 ->  {
                    System.out.println("Enter the id of the GuestGroup.");
                    int index= readScanner.nextInt();
                    GuestGroup group= service.getGroup(index);
                    if(group==null){
                        System.out.println("The index introduced is not a valid one!");
                        audit.log("FAILED_Cancel_Reservation");
                    }
                    else{
                        service.cancelReservation(index);
                        audit.log("Cancel_Reservation");
                    }
                    break;
                }
                case 2 -> {
                    System.out.println("Enter the id of the GuestGroup.");
                    int index= readScanner.nextInt();
                    readScanner.nextLine();
                    GuestGroup group= service.getGroup(index);
                    if(group==null){
                        System.out.println("The index introduced is not a valid one!");
                        audit.log("FAILED_Show_Group");
                    }
                    else{
                        System.out.println("-------------------------------------------------------------");
                        System.out.println(group);
                        System.out.println("-------------------------------------------------------------");
                        audit.log("Show_Group");
                    }
                    break;
                }
                case 3 ->  {
                    System.out.println("-------------------------------------------------------------");
                    for(Integer id: service.getAllGroups().keySet())
                        System.out.println(service.getGroup(id));
                    System.out.println("-------------------------------------------------------------");
                    audit.log("Show_All_Groups");
                    break;
                }
                case 4 ->{
                    System.out.println("Enter the id of the GuestGroup.");
                    int index= readScanner.nextInt();
                    readScanner.nextLine();
                    double cost= service.totalPriceForGroup(index);
                    if(cost== -1){
                        System.out.println("The Id that you've entered, "+ index+ " is not a valid id.");
                        audit.log("FAILED_Show_Cost");
                    }
                    else{
                        System.out.println("The total cost for the group with the Id "+ index+ " is "+ cost+ "\n");
                        audit.log("Show_Cost");
                    }
                    break;
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
                    audit.log("Show_All_Costs_Orderly");
                    break;
                }
                case 6 ->{
                    System.out.println("Please enter the 3 characters string that represent the month.");
                    String label= readScanner.nextLine().strip();
                    try{
                        Months month= Months.fromLabel(label);
                        System.out.println("The winnings for the month of "+ month+ " are "+service.getTotalWinningsForTheMonth(month)+ ".\n");
                        audit.log("Show_Winnings_in_a_month");
                    }
                    catch(Exception e){
                        System.out.println(e.getMessage());
                        audit.log("FAILED_Show_Winnings_in_a_month");
                    }
                    
                    break;
                }
                case 7 ->{
                    System.out.println("Please enter the 3 characters string that represent the month.");
                    String label= readScanner.nextLine().strip();
                    try{
                        Months month= Months.fromLabel(label);
                        System.out.println("Please enter the type of room that you want to see the occupation percantage for.\n For normals 0\n For apartments 1\n For individuals 2\n For scenes otherwise.\n");
                        int index= readScanner.nextInt();
                        readScanner.nextLine();
                        Room room = switch(index) {
                            case 0 -> new NormalRoom();
                            case 1 -> new ApartmentRoom();
                            case 2 -> new IndividualSpectacleRoom();
                            default -> new SceneSpectacleRoom();
                        };
                        System.out.println("The percentage of occupation for the month of "+ month+ " for "+ (index==0?"Normals":index==1?"Apartments":index==2?"Individuals":"Scenes") + " is  "+ service.getPercentagePfOccupationPerMonthTypeRoom(month, room)+ ".\n");
                        audit.log("Show_Percentage_Of_Occupation");
                    }catch(Exception e){
                        System.out.println(e.getMessage());
                        audit.log("FAILED_Show_Percentage_Of_Occupation");
                    }
                    break;
                }
                case 8 ->{
                    audit.cleanAudit();
                    audit.log("Cleaned_Audit");
                    System.out.println("Audit cleaned up!\n");
                    break;
                }
                case 9 ->  {
                    continueWithGuestsGroups= false;
                    System.out.println("We hope you had a good experience with our app!\nHave a good day!");
                    audit.log("Exit");
                    roomDays= new ArrayList<>();
                    for(int i=0; i<hotel.getNrApartments()+hotel.getNrNormals()+hotel.getNrIndividuals()+hotel.getNrScenes()+1; ++i){
                        ArrayList<Integer> days= new ArrayList<>();
                        if(i== hotel.getNrApartments()+hotel.getNrNormals()+hotel.getNrIndividuals()+hotel.getNrScenes())
                            for(int j=1; j<366; ++j)
                                days.add(hotel.getRestaurant().getNrOfGuestsThatDay(j));
                        else
                            for(int j=1; j<366; ++j)
                                days.add(hotel.getRoom(i).getIdDays(j));
                           
                        roomDays.add(days);
                    }
                    hotelDAO.update(new HotelConfig(fileService, roomDays), config.getId());
                    audit.close();
                    break;
                }
                default -> {
                    System.out.println("You've entered an invalid option.");
                    audit.log("INVALID_INPUT");
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