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

    public void readHotelDetails(Scanner sc){
        this.hotel.read(sc);
    }

    public int readGuestGroupDetails(Scanner sc){
        GuestGroup holder = new GuestGroup(Service.index++);
        holder.read(sc);
        this.groups.put(holder.getId(), holder);
        return Service.index-1;
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

    private int placeGuests(GuestGroup group, int i, ArrayList<Integer> array, int cap, int nrOfRooms){
        int k=0;
        while(k< nrOfRooms&& i<group.getNrOfGuests()){
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
            (group.getSpectacleRoomType()== SpectacleRoomType.INDIVIDUAL&&
            ((group.getNrOfGuests()< IndividualSpectacleRoom.getMinCap())||
            neccSpectacleRooms> this.hotel.getNrIndividuals()))||
            (group.getSpectacleRoomType()== SpectacleRoomType.SCENE&&
            ((group.getNrOfGuests()< SceneSpectacleRoom.getMinCap())||
            neccSpectacleRooms> this.hotel.getNrScenes()));
    }

    public void makeReservation(int groupId){
        GuestGroup group= this.groups.get(groupId);
        int nrOfGuestsWithJacuzzi= 0, mDay= 1, nrOfApsJacuzzis= 0, x= this.hotel.getNrApartments()+ this.hotel.getNrNormals()+ this.hotel.getNrIndividuals()+this.hotel.getNrScenes();
        int neccSpectacleRooms= group.getSpectacleRoomType()== SpectacleRoomType.INDIVIDUAL?
                        (int)Math.ceil(1.0*group.getNrOfGuests()/IndividualSpectacleRoom.getMaxCap()):
                        (group.getSpectacleRoomType()== SpectacleRoomType.SCENE?
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
                this.groups.get(groupId-1).setState(-1);
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
                if(group.getSpectacleRoomType()== SpectacleRoomType.INDIVIDUAL){
                    result= free(new IndividualSpectacleRoom(), mDay, bigMinDay, arrayIndivs, group.getNrOfDays(), 0);
                    bigMinDay= result.getX();
                    if(arrayIndivs.size()< neccSpectacleRooms)
                        accept= false;
                }
                else
                    if(group.getSpectacleRoomType()== SpectacleRoomType.SCENE){
                        result= free(new SceneSpectacleRoom(), mDay, bigMinDay, arrayScenes, group.getNrOfDays(), 0);
                        bigMinDay= result.getX();
                        if(arrayScenes.size()< neccSpectacleRooms)
                            accept= false;
                    }
                result= free(mDay, bigMinDay, group.getNrOfDays(), nrOfBreakfasts);
                bigMinDay= result.getX();
                accept= result.getY()== 1&& accept;
                if(((arrayAparts.size()+ arrayApsWithJacuzzis.size())* ApartmentRoom.getMaxCap()+ arrayNormals.size()* NormalRoom.getMaxCap()>= group.getNrOfGuests()&& arrayApsWithJacuzzis.size()*ApartmentRoom.getMaxCap()>=nrOfGuestsWithJacuzzi)&& accept){
                    found= true;
                    result= chooseBestRoomCombo(group.getNrOfGuests(), nrOfGuestsWithJacuzzi, arrayNormals.size(),arrayAparts.size(), arrayApsWithJacuzzis.size());
                    group.setNrOfNormals(result.getX());
                    group.setNrOfApartments(result.getY()+result.getZ());
                    group.setMinDay(mDay);
                    group.setNrOfIndividuals(Math.min(arrayIndivs.size(), neccSpectacleRooms));
                    group.setNrOfScenes(Math.min(arrayScenes.size(), neccSpectacleRooms));
                    ocuppyRooms(new NormalRoom(), group, result.getX(), arrayNormals, 0);
                    ocuppyRooms(new ApartmentRoom(), group, result.getZ(), arrayApsWithJacuzzis, 0);
                    ocuppyRooms(new ApartmentRoom(), group, result.getY(), arrayAparts, result.getZ());
                    ocuppyRooms(new IndividualSpectacleRoom() ,group, group.getNrOfIndividuals(), arrayIndivs, 0);
                    ocuppyRooms(new SceneSpectacleRoom(), group, group.getNrOfScenes(), arrayScenes, 0);
                    this.hotel.getRestaurant().setIdDays(nrOfBreakfasts, group.getMinDay(), group.getNrOfDays());
                    group.sort();
                    i= 0;
                    i= placeGuests(group, i, arrayApsWithJacuzzis, ApartmentRoom.getMaxCap(), result.getZ());
                    i= placeGuests(group, i, arrayAparts, ApartmentRoom.getMaxCap(), result.getY());
                    placeGuests(group, i, arrayNormals, NormalRoom.getMaxCap(), result.getX());
                }
                else
                    mDay= bigMinDay;
            }
        }
        this.groups.put(groupId, group);
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

        if(group.getSpectacleRoomType()!=SpectacleRoomType.NONE){
            if(group.getNrOfIndividuals()!=0)
                System.out.println(group.getNrOfIndividuals()+" Individual Spectacle Rooms");
            if(group.getNrOfScenes()!=0)
                System.out.println(group.getNrOfScenes()+" Scene Spectacle Rooms");
        }
        this.groups.put(groupId, group);
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
            String pref= group.getGuest(i).getPreferedMenu();
            for(int j=0; j< this.hotel.getRestaurant().getNrOfMenus(); ++j)
                if(this.hotel.getRestaurant().getMenus(j).getName().equalsIgnoreCase(pref)){
                    price+= this.hotel.getRestaurant().getMenus(j).getCost();
                    break;
                }
        }
        return price;
    }
    public Map<Integer, GuestGroup> sortedByTotalPrice(int order) {
        List<Map.Entry<Integer, GuestGroup>> entryList = new ArrayList<>(this.groups.entrySet());
        Collections.sort(entryList, (e1, e2) -> {
            double price1 = totalPriceForGroup(e1.getValue().getId());
            double price2 = totalPriceForGroup(e2.getValue().getId());
            return order > 0 ? Double.compare(price1, price2) : Double.compare(price2, price1);
        });
        
        Map<Integer, GuestGroup> orderedList = new LinkedHashMap<>();
        for (Map.Entry<Integer, GuestGroup> entry : entryList) {
            orderedList.put(entry.getKey(), entry.getValue());
        }
        
        return orderedList;
    }

}