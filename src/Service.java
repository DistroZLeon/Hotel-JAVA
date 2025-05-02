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
                        if(this.hotel.getRestaurant().getMenus(k).getName().equals(holder.getGuest(i).getPrefferedMenus(j))){
                            exists=true;
                            break;
                        }
                    }
                if(!exists){ 
                    holder.getGuest(i).notWantsAtRestaurant();
                    System.out.println("There is no menu in the restaurant that has any of the guest's prefferences. Because of this, the guest will automatically dine in the room.\n");
                }   
            }
        }
        this.groups.add(holder);
    }

    private class ReturnType{
        private int x, y, z;
        public int getX() {
            return this.x;
        }
        public void setX(int x) {
            this.x = x;
        }
        public int getY() {
            return this.y;
        }
        public void setY(int y) {
            this.y = y;
        }
        public int getZ() {
            return this.z;
        }
        public void setZ(int z) {
            this.z = z;
        }
    }

    private ReturnType free(Room room, int mDay, int bigMinDay, ArrayList<Integer> list, int xNrOfDays, int nrOfJacuzzis){
        ReturnType obj= new ReturnType();
        int i, j, k, good, start=0, limit, nrJac;
        while(!room.getClass().equals(this.hotel.getRoom(start).getClass())){
            start++;
        }            
        limit= start+ this.hotel.getNrOfThatRoom(room);
        for(i= start; i< limit; ++i){
            for(j= mDay; j< 366- xNrOfDays; ++j){
                good= 1;
                nrJac= 0;
                for(k= j; k< j+ xNrOfDays; ++k){
                    if(this.hotel.getRoom(i).getIdDays(k)!= 0)
                        good= 0;
                    if(room instanceof ApartmentRoom&& ((ApartmentRoom)this.hotel.getRoom(i)).getHasJacuzzi())
                        nrJac++;
                }
                if(room instanceof ApartmentRoom&& ((ApartmentRoom)this.hotel.getRoom(i)).getHasJacuzzi()){
                    if(good== 1&& nrJac* ApartmentRoom.getMaxCap()>= nrOfJacuzzis)
                        break;
                }
                else
                    if(good== 1)
                        break;
            }
            if(j== mDay)
                list.add(i);
            else
                if(j< bigMinDay)bigMinDay= j;
        }
        obj.setX(bigMinDay);
        return obj;
    }

    private ReturnType free(int mDay, int bigMinDay, int xNrOfDays, int nrOfGuests){
        ReturnType obj= new ReturnType();
        int j, k, good, mem= 1;
        for(j= mDay; j< 366- xNrOfDays; ++j){
            good= 1;
            for(k= j; k< j+ xNrOfDays; ++k){
                if(this.hotel.getRestaurant().getNrOfGuestsThatDay(k)+nrOfGuests>this.hotel.getRestaurant().getNrOfSeats())
                    good= 0;
            }
            if(good== 1)break;
        }
        if(j!= mDay){
            mem= 0;
            if(j< bigMinDay)
                bigMinDay= j;
        }
        obj.setX(bigMinDay);
        obj.setY(mem);
        return obj;
    }

    private ReturnType chooseBestRoomCombo(int nrGuests, int nrWantJacuzzi, int nrNormals, int nrApsNoJacuzzi, int nrApsWithJacuzzi) {
        ReturnType obj= null;
        int minTotalRooms= Integer.MAX_VALUE;
        
        for (int apsWithJac= 0; apsWithJac<= nrApsWithJacuzzi; apsWithJac++) {
            if (apsWithJac* ApartmentRoom.getMaxCap()< nrWantJacuzzi)
                continue;
            int remainingGuests= nrGuests- Math.min(apsWithJac* ApartmentRoom.getMaxCap(), nrWantJacuzzi);
            for (int apsNoJac= 0; apsNoJac<= nrApsNoJacuzzi; apsNoJac++) {
                int guestsLeft= remainingGuests- apsNoJac* ApartmentRoom.getMaxCap();
                if (guestsLeft< 0) guestsLeft= 0;

                int neededNormals= (int)Math.ceil(guestsLeft* 1.0/ NormalRoom.getMaxCap());
                if (neededNormals> nrNormals)
                    continue;

                int totalRooms= apsWithJac+ apsNoJac+ neededNormals;
                if (totalRooms< minTotalRooms) {
                    minTotalRooms= totalRooms;
                    obj= new ReturnType();
                    obj.setX(neededNormals);
                    obj.setY(apsNoJac);
                    obj.setZ(apsWithJac);
                }
            }
            if (obj!= null)
                break;
        }
        return obj;
    }

    private int placeGuests(GuestGroup group, int i, ArrayList<Integer> array, int cap){
        int k=0;
        while(k< array.size()&& i<group.getNrOfGuests()){
            for (int j= 0; j< cap&& i< group.getNrOfGuests(); ++j)
                group.getGuest(i++).setRoomIndex(this.hotel.getRoom(array.get(k)).getIndex());
            ++k;
        }
        return i;
    }
    private void ocuppyRooms(Room room, GuestGroup group, int nrOfRooms, ArrayList<Integer> array, int offset){
        for(int k= 0; k< nrOfRooms; ++k){
            if(room instanceof NormalRoom)
                group.setNormalsArray(array.get(k), k+ offset);
            else
                if(room instanceof ApartmentRoom)
                    group.setApartmentsArray(array.get(k), k+ offset);
                else
                    if(room instanceof IndividualSpectacleRoom)
                        group.setIndividualsArray(array.get(k), k+ offset);
                    else
                        group.setScenesArray(array.get(k), k+ offset);
            this.hotel.getRoom(array.get(k)).setIdDays(group.getId(), group.getMinDay(), group.getNrOfDays());
        }
    }

    private boolean verifyGroupValidity(GuestGroup group,int nrOfGuestsWithJacuzzi, int nrOfApsJacuzzis, int neccSpectacleRooms){
       return nrOfGuestsWithJacuzzi> nrOfApsJacuzzis* ApartmentRoom.getMaxCap()||
            group.getNrOfGuests()> ApartmentRoom.getMaxCap()*this.hotel.getNrApartments()+NormalRoom.getMaxCap()* this.hotel.getNrNormals()||
            (group.getSpectacleRoomType().equals("Individual")&&
            ((group.getNrOfGuests()< IndividualSpectacleRoom.getMinCap())||
            neccSpectacleRooms> this.hotel.getNrIndividuals()))||
            (group.getSpectacleRoomType().equals("Scene")&&
            ((group.getNrOfGuests()< SceneSpectacleRoom.getMinCap())||
            neccSpectacleRooms> this.hotel.getNrScenes()));
    }

    public void makeReservation(int groupId){
        GuestGroup group= this.groups.get(groupId);
        int nrOfGuestsWithJacuzzi= 0, mDay= 1, nrOfApsJacuzzis= 0, x= this.hotel.getNrApartments()+ this.hotel.getNrNormals()+ this.hotel.getNrIndividuals()+this.hotel.getNrScenes();
        int neccSpectacleRooms= group.getSpectacleRoomType().equals("Individual")?
                        (int)Math.ceil(1.0*group.getNrOfGuests()/IndividualSpectacleRoom.getMaxCap()):
                        (group.getSpectacleRoomType().equals("Scene")?
                        (int)Math.ceil(1.0*group.getNrOfGuests()/SceneSpectacleRoom.getMaxCap()):0);
        boolean found= false;
        int nrOfBreakfasts= 0;
        for(int i=0; i< group.getNrOfGuests(); ++i){
            if(group.getGuest(i).isWantsJacuzzi())
                nrOfGuestsWithJacuzzi++;
            if(group.getGuest(i).isWantsAtRestaurant())
                nrOfBreakfasts++;
        }
        
        for(int i=0; i< x; ++i)
            if(this.hotel.getRoom(i) instanceof ApartmentRoom)
                nrOfApsJacuzzis++;
        if(verifyGroupValidity(group, nrOfGuestsWithJacuzzi, nrOfApsJacuzzis, neccSpectacleRooms)){
                this.groups.get(groupId).setState(-1);
                System.out.println("Some validity conditions for the group are not checked.\n");
        }
        else{
            while(!found){
                ArrayList<Integer> arrayAparts= new ArrayList<>(), arrayNormals= new ArrayList<>(), arrayIndivs= new ArrayList<>(), arrayScenes= new ArrayList<>(), arrayApsWithJacuzzis= new ArrayList<>();
                int i, bigMinDay=366;
                boolean accept;
                ReturnType result;
                result= free(new NormalRoom(), mDay, bigMinDay, arrayNormals, group.getNrOfDays(), 0);
                bigMinDay= result.getX();
                result= free(new ApartmentRoom(), mDay, bigMinDay, arrayAparts, group.getNrOfDays(), nrOfGuestsWithJacuzzi);
                bigMinDay= result.getX();
                i=0;
                while(i< arrayAparts.size()){
                    if(((ApartmentRoom)this.hotel.getRoom(arrayAparts.get(i))).getHasJacuzzi()){
                        arrayApsWithJacuzzis.add(arrayAparts.get(i));
                        arrayAparts.remove(i);
                    }
                    else
                        i++;
                }
                accept= true;
                if(group.getSpectacleRoomType().equals("Individual")){
                    result= free(new IndividualSpectacleRoom(), mDay, bigMinDay, arrayIndivs, group.getNrOfDays(), 0);
                    bigMinDay= result.getX();
                    if(arrayIndivs.size()< neccSpectacleRooms)
                        accept= false;
                }
                else
                    if(group.getSpectacleRoomType().equals("Scene")){
                        result= free(new SceneSpectacleRoom(), mDay, bigMinDay, arrayScenes, group.getNrOfDays(), 0);
                        bigMinDay= result.getX();
                        if(arrayScenes.size()< neccSpectacleRooms)
                            accept= false;
                    }
                result= free(mDay, bigMinDay, group.getNrOfDays(), nrOfBreakfasts);
                bigMinDay= result.getX();
                accept= result.getY()== 1;
                if(arrayAparts.size()* ApartmentRoom.getMaxCap()+ arrayNormals.size()* NormalRoom.getMaxCap()>= group.getNrOfGuests()&& accept){
                    found= true;
                    result= chooseBestRoomCombo(group.getNrOfGuests(), nrOfGuestsWithJacuzzi, arrayNormals.size(),arrayAparts.size(), arrayApsWithJacuzzis.size());
                    group.setNrOfNormals(result.getX());
                    group.setNrOfApartments(result.getY()+result.getZ());
                    group.setMinDay(bigMinDay);
                    group.setNrOfIndividuals(Math.min(arrayIndivs.size(), neccSpectacleRooms));
                    group.setNrOfScenes(Math.min(arrayScenes.size(), neccSpectacleRooms));
                    ocuppyRooms(new NormalRoom(), group, result.getX(), arrayNormals, 0);
                    ocuppyRooms(new ApartmentRoom(), group, result.getZ(), arrayApsWithJacuzzis, 0);
                    ocuppyRooms(new ApartmentRoom(), group, result.getY(), arrayAparts, result.getZ());
                    ocuppyRooms(new IndividualSpectacleRoom() ,group, group.getNrOfIndividuals(), arrayIndivs, result.getZ());
                    ocuppyRooms(new SceneSpectacleRoom(), group, group.getNrOfScenes(), arrayScenes, result.getZ());
                    this.hotel.getRestaurant().setIdDays(nrOfBreakfasts, group.getMinDay(), group.getNrOfDays());
                    group.sort();
                    i= 0;
                    i= placeGuests(group, i, arrayApsWithJacuzzis, ApartmentRoom.getMaxCap());
                    i= placeGuests(group, i, arrayAparts, ApartmentRoom.getMaxCap());
                    i= placeGuests(group, i, arrayNormals, NormalRoom.getMaxCap());
                }
                else
                    mDay= bigMinDay;
            }
        }
        this.groups.set(groupId, group);
    }

    public void cancelReservation(int groupId){
        GuestGroup group= this.groups.get(groupId);
        group.setState(0);
        for(int i=0; i< group.getNrOfNormals(); ++i){
            this.hotel.getRoom(group.getNormalsArray(i)).setIdDays(0,group.getMinDay(), group.getNrOfDays());
        }
        for(int i=0; i< group.getNrOfApartments(); ++i){
            this.hotel.getRoom(group.getApartmentsArray(i)).setIdDays(0,group.getMinDay(), group.getNrOfDays());
        }
        for(int i=0; i< group.getNrOfIndividuals(); ++i){
            this.hotel.getRoom(group.getIndividualsArray(i)).setIdDays(0,group.getMinDay(), group.getNrOfDays());
        }
        for(int i=0; i< group.getNrOfScenes(); ++i){
            this.hotel.getRoom(group.getScenesArray(i)).setIdDays(0,group.getMinDay(), group.getNrOfDays());
        }

        int nrOfBreakfasts= 0;
        for(int i=0; i< group.getNrOfGuests(); ++i)
            if(group.getGuest(i).isWantsAtRestaurant())
                nrOfBreakfasts++;
        this.hotel.getRestaurant().setIdDays(-nrOfBreakfasts, group.getMinDay(), group.getNrOfDays());

        System.out.println("The group with the id "+group.getId()+" was cancelled!\nThe group had had reserved: ");
        if(group.getNrOfNormals()!=0)
            System.out.println(group.getNrOfNormals()+" Normal Rooms");
        if(group.getNrOfApartments()!=0)
            System.out.println(group.getNrOfApartments()+" Apartment Rooms");

        if(!group.getSpectacleRoomType().equals("None")){
            if(group.getNrOfIndividuals()!=0)
                System.out.println(group.getNrOfIndividuals()+" Individual Spectacle Rooms");
            if(group.getNrOfScenes()!=0)
                System.out.println(group.getNrOfScenes()+" Scene Spectacle Rooms");
        }
        this.groups.set(groupId, group);
    }
}