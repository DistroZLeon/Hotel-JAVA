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
        private int nrOfRooms, bigMinDay;

        public int getNrOfRooms() {
            return this.nrOfRooms;
        }

        public void setNrOfRooms(int nrOfRooms) {
            this.nrOfRooms = nrOfRooms;
        }

        public int getBigMinDay() {
            return this.bigMinDay;
        }

        public void setBigMinDay(int bigMinDay) {
            this.bigMinDay = bigMinDay;
        }
    }
    private Out free(Room room, int mDay, int bigMinDay, ArrayList<Integer> list, int xNrOfDays, int nrOfJacuzzis){
        Out obj= new Out();
        int i, j, k, good, start=0, limit, nrOfRooms=0, nrJac;
        while(!room.getClass().equals(this.hotel.getRoom(start).getClass())){
            start++;
        }            
        limit= start+ this.hotel.getNrOfThatRoom(room);
        for(i=start;i<limit;++i){
            for(j=mDay;j<366-xNrOfDays;++j){
                good=1;
                nrJac=0;
                for(k=j;k<j+xNrOfDays;++k){
                    if(this.hotel.getRoom(i).getIdDays(k)!=0)good=0;
                    if(room instanceof ApartmentRoom && ((ApartmentRoom)this.hotel.getRoom(i)).getHasJacuzzi())nrJac++;
                }
                if(room instanceof ApartmentRoom && ((ApartmentRoom)this.hotel.getRoom(i)).getHasJacuzzi()){
                    if(good==1 && nrJac*ApartmentRoom.getMaxCap()>=nrOfJacuzzis)break;
                }
                else
                    if(good==1)break;
            }
            if(j==mDay){
                list.add(i);
                nrOfRooms++;
            }
            else
                if(j<bigMinDay)bigMinDay=j;
        }
        obj.setBigMinDay(bigMinDay);
        obj.setNrOfRooms(nrOfRooms);
        return obj;
    }
    private Out free(int mDay, int bigMinDay, int xNrOfDays, int nrOfGuests){
        Out obj= new Out();
        int j, k, good, mem=1;
        for(j=mDay;j<366-xNrOfDays;++j){
            good=1;
            for(k=j;k<j+xNrOfDays;++k){
                if(this.hotel.getRestaurant().getNrOfGuestsThatDay(k)+nrOfGuests>this.hotel.getRestaurant().getNrOfSeats())good=0;
            }
            if(good==1)break;
        }
        if(j!=mDay){
            mem=0;
            if(j<bigMinDay)bigMinDay=j;
        }
        obj.setBigMinDay(bigMinDay);
        obj.setNrOfRooms(mem);
        return obj;
    }
    public void makeReservation(int index){
        GuestGroup group= this.groups.get(index);
        int nrOfGuestsWithJacuzzi= 0, mDay= 1, nrOfApsJacuzzis= 0, x= this.hotel.getNrApartments()+ this.hotel.getNrNormals()+ this.hotel.getNrIndividuals()+this.hotel.getNrScenes();
        int neccSpectacleRooms= group.getSpectacleRoomType().equals("Individual")?(int)Math.ceil(1.0*group.getNrOfGuests()/IndividualSpectacleRoom.getMaxCap()):(int)Math.ceil(1.0*group.getNrOfGuests()/SceneSpectacleRoom.getMaxCap());
        boolean found= false;
        int nrOfBreakfasts=0;
        for(int i=0; i<group.getNrOfGuests(); ++i){
            if(group.getGuest(i).isWantsJacuzzi())
                nrOfGuestsWithJacuzzi++;
            if(group.getGuest(i).isWantsAtRestaurant())
                nrOfBreakfasts++;
        }
        
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
                ArrayList<Integer> arrayAparts= new ArrayList<>(), arrayNormals= new ArrayList<>(), arrayIndivs= new ArrayList<>(), arrayScenes= new ArrayList<>();
                int i, j, k, bigMinDay=366, nrAps, nrNorms, nrIndivs, nrScenes;
                boolean accept;
                Out result;
                result= free(new NormalRoom(), mDay, bigMinDay, arrayNormals, group.getNrOfDays(), 0);
                bigMinDay= result.getBigMinDay();
                nrNorms= result.getNrOfRooms();
                result= free(new ApartmentRoom(), mDay, bigMinDay, arrayAparts, group.getNrOfDays(), nrOfGuestsWithJacuzzi);
                bigMinDay= result.getBigMinDay();
                nrAps= result.getNrOfRooms();
                accept=true;
                if(group.getSpectacleRoomType().equals("Individual")){
                    result= free(new IndividualSpectacleRoom(), mDay, bigMinDay, arrayIndivs, group.getNrOfDays(), 0);
                    bigMinDay= result.getBigMinDay();
                    nrIndivs= result.getNrOfRooms();
                    if(nrIndivs< neccSpectacleRooms)
                        accept= false;
                }
                else
                    if(group.getSpectacleRoomType().equals("Scene")){
                        result= free(new SceneSpectacleRoom(), mDay, bigMinDay, arrayScenes, group.getNrOfDays(), 0);
                        bigMinDay= result.getBigMinDay();
                        nrScenes= result.getNrOfRooms();
                        if(nrScenes< neccSpectacleRooms)
                            accept= false;
                    }
                result= free(mDay, bigMinDay, group.getNrOfDays(), nrOfBreakfasts);
                bigMinDay= result.getBigMinDay();
                accept= result.getNrOfRooms()==1;
                if(nrAps*ApartmentRoom.getMaxCap()+ nrNorms*NormalRoom.getMaxCap()>= group.getNrOfGuests()&& accept){
                    found= true;
                    
                }
            }
        }
    }
}
