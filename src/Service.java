import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Service {
    private static Service instance;
    private final Hotel hotel;
    private final Map<Integer, GuestGroup> groups;
    private static int index=1;

    private Service(){
        this.hotel= Hotel.getInstance();
        this.groups= new HashMap<>();
    }

    public static Service getInstance() {
        if (instance == null) {
            instance = new Service();
        }
        return instance;
    }

    public GuestGroup getGroup(int index){
        return this.groups.get(index);
    }

    public Map<Integer, GuestGroup> getAllGroups(){
        return this.groups;
    }

    public void readHotelDetails(Scanner sc){
        this.hotel.read(sc);
    }

    public int readGuestGroupDetails(Scanner sc){
        GuestGroup holder = new GuestGroup(Service.index++);
        holder.read(sc);
        this.groups.put(holder.getId(), holder);
        return holder.getId();
    }
    public int getIndex(){
        return Service.index;
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
    //This function's role is to get the indexes of the rooms that have the type of the prameter room, that are empty
    // in the interval mDay and mDay+xNrOfDays-1. Also to return the earliest day that there are enough seats for
    // all the guests
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
    //Main function of this procedure is to verify if there are enough seats in the restaurants for all the guests
    // that want to have the breakfast at the restaurant, and return the earliest day that there are enough seats for
    // all the guests
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
    //Function to choose best combination of Normal Rooms, Apartment Rooms with Jacuzzis and Apartment Rooms with no Jacuzzis 
    private ReturnType chooseBestRoomCombo(int nrGuests, int nrWantJacuzzi, int nrNormals, int nrApsNoJacuzzi, int nrApsWithJacuzzi) {
        ReturnType obj = null;
        int minTotalRooms = Integer.MAX_VALUE;
        int maxCapApt = ApartmentRoom.getMaxCap();
        int maxCapNorm = NormalRoom.getMaxCap();
        for (int apsWithJac = 0; apsWithJac <= nrApsWithJacuzzi; apsWithJac++) {
            int capWithJac = apsWithJac * maxCapApt;
            if (capWithJac < nrWantJacuzzi)
                continue;
            for (int apsNoJac = 0; apsNoJac <= nrApsNoJacuzzi; apsNoJac++) {
                int capNoJac = apsNoJac * maxCapApt;
                int totalCapSoFar = capWithJac + capNoJac;
                int guestsLeft = nrGuests - totalCapSoFar;
                if (guestsLeft < 0)
                    guestsLeft = 0;
                int neededNormals = (int) Math.ceil(guestsLeft * 1.0 / maxCapNorm);
                if (neededNormals > nrNormals)
                    continue;
                int jacuzziWaste = capWithJac - nrWantJacuzzi;
                int totalRooms = apsWithJac + apsNoJac + neededNormals;
                boolean isBetter = false;
                if (obj == null) {
                    isBetter = true;
                } else if (totalRooms < minTotalRooms) {
                    isBetter = true;
                } else if (totalRooms == minTotalRooms && jacuzziWaste < (obj.getZ() * maxCapApt - nrWantJacuzzi)) {
                    isBetter = true;
                }
                if (isBetter) {
                    minTotalRooms = totalRooms;
                    obj = new ReturnType();
                    obj.setX(neededNormals);
                    obj.setY(apsNoJac);
                    obj.setZ(apsWithJac);
                }
            }
        }
        return obj;
    }
    //This function decides where each user will stay
    private int placeGuests(GuestGroup group, int i, ArrayList<Integer> array, int cap, int nrOfRooms){
        int k=0;
        while(k< nrOfRooms&& i<group.getNrOfGuests()){
            for (int j= 0; j< cap&& i< group.getNrOfGuests(); ++j)
                group.getGuest(i++).setRoomIndex(this.hotel.getRoom(array.get(k)).getIndex());
            ++k;
        }
        return i;
    }
    //This function marks the Rooms of the type of the room parameter that that group has reserved as occupyed by
    // the group 
    private void occupyRooms(Room room, GuestGroup group, int nrOfRooms, ArrayList<Integer> array, int offset){
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
    //Verification of the validity conditions for the group in relation to the state of the hotel. For example if there
    //are more guests than there can exists in the hotel etc.
    private boolean verifyGroupValidity(GuestGroup group,int nrOfGuestsWithJacuzzi, int nrOfApsJacuzzis, int necSpectacleRooms){
       System.out.println((group.getSpectacleRoomType()== SpectacleRoomType.SCENE&&
       (group.getNrOfGuests()>= SceneSpectacleRoom.getMinCap()&&
       necSpectacleRooms<= this.hotel.getNrScenes())));
        return nrOfGuestsWithJacuzzi<= nrOfApsJacuzzis* ApartmentRoom.getMaxCap()&&
            group.getNrOfGuests()<= ApartmentRoom.getMaxCap()*this.hotel.getNrApartments()+NormalRoom.getMaxCap()* this.hotel.getNrNormals()&&
            ((group.getSpectacleRoomType()== SpectacleRoomType.INDIVIDUAL&&
            (group.getNrOfGuests()>= IndividualSpectacleRoom.getMinCap()&&
            necSpectacleRooms<= this.hotel.getNrIndividuals()))||
            (group.getSpectacleRoomType()== SpectacleRoomType.SCENE&&
            (group.getNrOfGuests()>= SceneSpectacleRoom.getMinCap()&&
            necSpectacleRooms<= this.hotel.getNrScenes()))||group.getSpectacleRoomType()== SpectacleRoomType.NONE);
    }

    public int makeReservation(int groupId, int theMinDay, int hotelId) throws SQLException{
        GuestGroupDAO groupDAO= GuestGroupDAO.getInstance();
        GuestGroup group= this.groups.get(groupId);
        int nrOfGuestsWithJacuzzi= 0, mDay= theMinDay, nrOfApsJacuzzis= 0, x= this.hotel.getNrApartments()+ this.hotel.getNrNormals()+ this.hotel.getNrIndividuals()+this.hotel.getNrScenes();
        // The necessary number of Spectacle Rooms based on the group's choice for the type of Spectacle Room
        int necSpectacleRooms= group.getSpectacleRoomType()== SpectacleRoomType.INDIVIDUAL?
                        (int)Math.ceil(1.0*group.getNrOfGuests()/IndividualSpectacleRoom.getMaxCap()):
                        (group.getSpectacleRoomType()== SpectacleRoomType.SCENE?
                        (int)Math.ceil(1.0*group.getNrOfGuests()/SceneSpectacleRoom.getMaxCap()):0);
        boolean found= false;
        int nrOfBreakfasts= 0;
        // Getting the number of: all the people that want to have breakfast at the restaurant, all the people
        // that want to have a Jacuzzi in room and all the apartments that have a Jacuzzi.
        for(int i=0; i< group.getNrOfGuests(); ++i){
            if(group.getGuest(i).isWantsJacuzzi())
                nrOfGuestsWithJacuzzi++;
            if(group.getGuest(i).isWantsAtRestaurant())
                nrOfBreakfasts++;
        }
        for(int i=0; i< x; ++i)
            if(this.hotel.getRoom(i) instanceof ApartmentRoom&& ((ApartmentRoom)this.hotel.getRoom(i)).getHasJacuzzi())
                nrOfApsJacuzzis++;
        // Verification of the validity of the request for reservation
        if(!verifyGroupValidity(group, nrOfGuestsWithJacuzzi, nrOfApsJacuzzis, necSpectacleRooms)){
            this.groups.remove(groupId);
            System.out.println("Some validity conditions for the group are not checked. There seems to not be either enough rooms for the stay or enough Spectacle Rooms for you group's dimension needs.\n");
            return groupId;
        }
        else{
            // Loop until the group has been allocated the proper number of rooms for its request
            while(!found){
                ArrayList<Integer> arrayAparts= new ArrayList<>(), arrayNormals= new ArrayList<>(), arrayIndivs= new ArrayList<>(), arrayScenes= new ArrayList<>(), arrayApsWithJacuzzis= new ArrayList<>();
                int i, bigMinDay=366;
                boolean accept;
                ReturnType result;
                // Getting the indexes of the free Normal rooms and Apartment rooms starting from the mDay
                result= free(new NormalRoom(), mDay, bigMinDay, arrayNormals, group.getNrOfDays(), 0);
                bigMinDay= result.getX();
                result= free(new ApartmentRoom(), mDay, bigMinDay, arrayAparts, group.getNrOfDays(), nrOfGuestsWithJacuzzi);
                bigMinDay= result.getX();
                i=0;
                // Splitting the array with the indexes of the Apartment rooms into Apartments with no Jacuzzis
                // and Apartments with Jacuzzis
                while(i< arrayAparts.size()){
                    if(((ApartmentRoom)this.hotel.getRoom(arrayAparts.get(i))).getHasJacuzzi()){
                        arrayApsWithJacuzzis.add(arrayAparts.get(i));
                        arrayAparts.remove(i);
                    }
                    else
                        i++;
                }
                accept= true;
                // Getting the indexes of the free Spectacel Rooms starting from the mDay, based
                //  on the group's Spectacle Room type specifications
                if(group.getSpectacleRoomType()== SpectacleRoomType.INDIVIDUAL){
                    result= free(new IndividualSpectacleRoom(), mDay, bigMinDay, arrayIndivs, group.getNrOfDays(), 0);
                    bigMinDay= result.getX();
                    if(arrayIndivs.size()< necSpectacleRooms)
                        accept= false;
                }
                else
                    if(group.getSpectacleRoomType()== SpectacleRoomType.SCENE){
                        result= free(new SceneSpectacleRoom(), mDay, bigMinDay, arrayScenes, group.getNrOfDays(), 0);
                        bigMinDay= result.getX();
                        if(arrayScenes.size()< necSpectacleRooms)
                            accept= false;
                    }
                // Verification if there are enough seats in the restaurant for all the guests that want to have breakfast
                //in the restaurant 
                result= free(mDay, bigMinDay, group.getNrOfDays(), nrOfBreakfasts);
                bigMinDay= result.getX();
                accept= result.getY()== 1&& accept;
                //If all the requirements are met, then mDay is the minimum day, otherwise, mDay= bigMinDay
                if(((arrayAparts.size()+ arrayApsWithJacuzzis.size())* ApartmentRoom.getMaxCap()+ arrayNormals.size()* NormalRoom.getMaxCap()>= group.getNrOfGuests()&& arrayApsWithJacuzzis.size()*ApartmentRoom.getMaxCap()>=nrOfGuestsWithJacuzzi)&& accept){
                    found= true;
                    // Gets best combination of the number of rooms
                    result= chooseBestRoomCombo(group.getNrOfGuests(), nrOfGuestsWithJacuzzi, arrayNormals.size(),arrayAparts.size(), arrayApsWithJacuzzis.size());
                    group.setNrOfNormals(result.getX());
                    group.setNrOfApartments(result.getY()+result.getZ());
                    group.setMinDay(mDay);
                    group.setNrOfIndividuals(Math.min(arrayIndivs.size(), necSpectacleRooms));
                    group.setNrOfScenes(Math.min(arrayScenes.size(), necSpectacleRooms));
                    // Occupying all the rooms necessary in the correct time frame
                    occupyRooms(new NormalRoom(), group, result.getX(), arrayNormals, 0);
                    occupyRooms(new ApartmentRoom(), group, result.getZ(), arrayApsWithJacuzzis, 0);
                    occupyRooms(new ApartmentRoom(), group, result.getY(), arrayAparts, result.getZ());
                    occupyRooms(new IndividualSpectacleRoom() ,group, group.getNrOfIndividuals(), arrayIndivs, 0);
                    occupyRooms(new SceneSpectacleRoom(), group, group.getNrOfScenes(), arrayScenes, 0);
                    this.hotel.getRestaurant().setIdDays(nrOfBreakfasts, group.getMinDay(), group.getNrOfDays());
                    group.sort();
                    i= 0;
                    // Places the  guests in the correct rooms
                    i= placeGuests(group, i, arrayApsWithJacuzzis, ApartmentRoom.getMaxCap(), result.getZ());
                    i= placeGuests(group, i, arrayAparts, ApartmentRoom.getMaxCap(), result.getY());
                    placeGuests(group, i, arrayNormals, NormalRoom.getMaxCap(), result.getX());
                }
                else{
                    if(theMinDay!=1){
                        System.out.println("The reservation request for the interval that you've entered, "+ theMinDay+ " and "+ (int)(theMinDay+group.getMinDay()-1)+ " is not available!");
                        this.groups.remove(groupId); 
                    }
                    else{
                    mDay= bigMinDay;
            }
                }
            }
            this.groups.remove(groupId);
            groupId= groupDAO.create(group, hotelId);
            group.setId(groupId);
            this.groups.put(groupId, group);
            return groupId;
        }
    }

    public void cancelReservation(int groupId) throws SQLException{
        GuestGroupDAO groupDAO= GuestGroupDAO.getInstance();
        GuestGroup group= this.groups.get(groupId);
        // Freeing all the rooms that the group has occupied
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
        //Getting the number of all the guests that will take the breakfast at the restaurant and setting
        // the number of seats occupied back to before this groups reservation
        int nrOfBreakfasts= 0;
        for(int i=0; i< group.getNrOfGuests(); ++i)
            if(group.getGuest(i).isWantsAtRestaurant())
                nrOfBreakfasts++;
        this.hotel.getRestaurant().setIdDays(-nrOfBreakfasts, group.getMinDay(), group.getNrOfDays());
        // Showing some details related to the reservation before cancelling
        System.out.println("The group with the id "+group.getId()+" was cancelled!\nThe group had had reserved: ");
        if(group.getNrOfNormals()!=0)
            System.out.println(group.getNrOfNormals()+" Normal Rooms");
        if(group.getNrOfApartments()!=0)
            System.out.println(group.getNrOfApartments()+" Apartment Rooms");

        if(group.getSpectacleRoomType()!=SpectacleRoomType.NONE){
            if(group.getNrOfIndividuals()!=0)
                System.out.println(group.getNrOfIndividuals()+" Individual Spectacle Rooms");
            if(group.getNrOfScenes()!=0)
                System.out.println(group.getNrOfScenes()+" Scene Spectacle Rooms");
        }
        this.groups.remove(groupId);
        groupDAO.delete(groupId);
    }
    public double totalPriceForGroup(int groupId){
        double price=0;
        GuestGroup group= this.groups.get(groupId);
        if(group==null)
            return -1;
        for(int i=0; i< group.getNrOfNormals(); ++i)
            price+=((NormalRoom)this.hotel.getRoom(group.getNormalsArray(i))).getTruePrice();
        for(int i=0; i< group.getNrOfApartments(); ++i)
            price+=((ApartmentRoom)this.hotel.getRoom(group.getApartmentsArray(i))).getTruePrice();
        for(int i=0; i< group.getNrOfIndividuals(); ++i)
            price+=((IndividualSpectacleRoom)this.hotel.getRoom(group.getIndividualsArray(i))).getTruePrice();
        for(int i=0; i< group.getNrOfScenes(); ++i)
            price+=((SceneSpectacleRoom)this.hotel.getRoom(group.getScenesArray(i))).getTruePrice();
        for(int i=0; i<group.getNrOfGuests(); ++i){
            String pref= group.getGuest(i).getPreferredMenu();
            for(int j=0; j< this.hotel.getRestaurant().getNrOfMenus(); ++j)
                if(this.hotel.getRestaurant().getMenus(j).getName().equalsIgnoreCase(pref)){
                    price+= this.hotel.getRestaurant().getMenus(j).getCost();
                    break;
                }
        }
        price= price* group.getNrOfDays();
        return price;
    }
    public Map<Integer, GuestGroup> sortedByTotalPrice(int order) {
        //Putting the map's entry's into a list so that the sorting can be done
        List<Map.Entry<Integer, GuestGroup>> entryList = new ArrayList<>(this.groups.entrySet());
        Collections.sort(entryList, (e1, e2) -> {
            double price1 = totalPriceForGroup(e1.getValue().getId());
            double price2 = totalPriceForGroup(e2.getValue().getId());
            return order > 0 ? Double.compare(price1, price2) : Double.compare(price2, price1);
        });
        //Using a LinkedHasMap so that there can exist an order related to the sorted list of entrys
        Map<Integer, GuestGroup> orderedList = new LinkedHashMap<>();
        for (Map.Entry<Integer, GuestGroup> entry : entryList) {
            orderedList.put(entry.getKey(), entry.getValue());
        }
        
        return orderedList;
    }
    public double getTotalWinningsForTheMonth(Months month){
        double total=0;
        for(Integer id: this.groups.keySet()){
            int minDay= this.groups.get(id).getMinDay();
            if(month.getStartDay()<= minDay&& minDay<= month.getEndDay()){
                int difference= (minDay+ this.groups.get(id).getNrOfDays()+ 1)- month.getEndDay();
                if(difference<=0)
                    total+= totalPriceForGroup(id);
                else
                    total+= ((this.groups.get(id).getNrOfDays()+ difference)/this.groups.get(id).getNrOfDays())*totalPriceForGroup(id);
            }
        }
        return total;
    }
    public double getPercentagePfOccupationPerMonthTypeRoom(Months month, Room room){
        double percentage=0;
        int start=0, limit;
        while(!room.getClass().equals(this.hotel.getRoom(start).getClass())){
            start++;
        }           
        limit= start+ this.hotel.getNrOfThatRoom(room);
        for(int i=start; i<limit; ++i){
            int occupiedDays=0;
            for(int j=month.getStartDay(); j<=month.getEndDay(); ++j)
                if(this.hotel.getRoom(i).getIdDays(j)!=0)
                    occupiedDays++;
            percentage+=1.0*occupiedDays/(month.getEndDay()- month.getStartDay()+ 1);
        }
        return 1.0*percentage/this.hotel.getNrOfThatRoom(room);
    }
    
    public void setupGroups(int hotelId) throws SQLException{
        GuestGroupDAO groupDAO= GuestGroupDAO.getInstance();
        ArrayList<GuestGroup> dbGroups= groupDAO.read(hotelId, 0);
        for(GuestGroup group:dbGroups){
            this.groups.put(group.getId(), group);
        }
    }
}