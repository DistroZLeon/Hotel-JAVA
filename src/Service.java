import java.util.ArrayList;
import java.util.Scanner;

public class Service {
    private final Hotel hotel;
    private final ArrayList<GuestGroup> groups;
    private static int index=1;
    public Service(){
        this.hotel= new Hotel();
        this.groups= new ArrayList<>();
    }
    public void readHotelDetails(Scanner sc){
        this.hotel.read(sc);
    }
    public void readGuestGroupDetails(Scanner sc){
        GuestGroup holder = new GuestGroup(Service.index++);
        holder.read(sc);
        for(int i=0; i<holder.getNrOfGuests(); ++i){
            if(holder.getGuest(i).isWantsAtRestaurant()){
                boolean exists= false;
                for(int j=0; j< holder.getGuest(i).getNrOfPrefferedMenus(); ++j)
                    for(int k=0; k< this.hotel.getRestaurant().getNrOfMenus(); ++k){
                        if(this.hotel.getRestaurant().getMenus(k).getName().equals(holder.getGuest(i).getPrefferedMenus(j)))
                            exists=true;
                        break;
                    }
                if(!exists){ 
                    holder.getGuest(i).notWantsAtRestaurant();
                    System.out.println("There is no menu in the restaurant that has any of the guest's prefferences. Because of this, the guest will automatically dine in the room.\n");
                }   
            }
        }
        this.groups.add(holder);
    }
    private class Out{
        private int nrOfAps, bigMinDay;
    }
    private Out free(Room room, int mDay, int bigMinDay, ArrayList<Integer> list, int xNrOfDays){
        Out obj= new Out();
        return obj;
    }
    public void makeReservation(int index){
        GuestGroup group= this.groups.get(index);
        int nrOfGuestsWithJacuzzi= 0, mDay= 1, nrOfApsJacuzzis= 0, x= this.hotel.getNrApartments()+ this.hotel.getNrNormals()+ this.hotel.getNrIndividuals()+this.hotel.getNrScenes();
        int neccSpectacleRooms= group.getSpectacleRoomType().equals("Individual")?(int)Math.ceil(1.0*group.getNrOfGuests()/IndividualSpectacleRoom.getMaxCap()):(int)Math.ceil(1.0*group.getNrOfGuests()/SceneSpectacleRoom.getMaxCap());
        boolean found= false;
        ArrayList<Integer> arrayAparts= new ArrayList<>(), arrayNormals= new ArrayList<>(), arrayIndivs= new ArrayList<>(), arrayScenes= new ArrayList<>();
        for(int i=0; i<group.getNrOfGuests(); ++i)
            if(group.getGuest(i).isWantsAtRestaurant())
                nrOfGuestsWithJacuzzi++;
        
        for(int i=0; i< x; ++i)
            if(this.hotel.getRoom(i) instanceof ApartmentRoom)
                nrOfApsJacuzzis++;
        if(nrOfGuestsWithJacuzzi>nrOfApsJacuzzis*ApartmentRoom.getMaxCap()||
            group.getNrOfGuests()>ApartmentRoom.getMaxCap()*this.hotel.getNrApartments()+NormalRoom.getMaxCap()*this.hotel.getNrNormals()||
            (group.getSpectacleRoomType().equals("Individual")&&
            ((group.getNrOfGuests()<IndividualSpectacleRoom.getMinCap())||
            neccSpectacleRooms>this.hotel.getNrIndividuals()))||
            (group.getSpectacleRoomType().equals("Scene")&&
            ((group.getNrOfGuests()<SceneSpectacleRoom.getMinCap())||
            neccSpectacleRooms>this.hotel.getNrScenes()))){
                this.groups.get(index).setState(-1);
                System.out.println("Some validity conditions for the group are not checked.\n");
        }
        else{
            while(!found){
                int i, j, k, bigMinDay=366;
                Out result= new Out();
                result= free(new NormalRoom(), mDay, bigMinDay, arrayNormals, group.getNrOfDays());
            }
        }
    }
}
