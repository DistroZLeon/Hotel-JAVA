import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Hotel {
    private static Hotel instance;
    private int nrOfApartments, nrOfNormals, nrOfScenes, nrOfIndividuals;
    private Room[] rooms;
    private final Map<Class<? extends Room>, Integer> roomCounts = new HashMap<>();
    private final Restaurant restaurant;

    public int getNrApartments() {
        return this.nrOfApartments;
    }

    public int getNrNormals() {
        return this.nrOfNormals;
    }

    public int getNrScenes() {
        return this.nrOfScenes;
    }

    public int getNrIndividuals() {
        return this.nrOfIndividuals;
    }

    public int getNrOfThatRoom(Room room){
        return roomCounts.getOrDefault(room.getClass(), 0);
    }

    public Restaurant getRestaurant() {
        return this.restaurant;
    }

    public Room getRoom(int i){
        return this.rooms[i];
    }

    private Hotel(){
        this.restaurant= new Restaurant();
    }

    public static Hotel getInstance() {
        if (instance == null) {
            instance = new Hotel();
        }
        return instance;
    }

    public void read(Scanner sc){
        this.nrOfNormals= sc.nextInt();
        this.nrOfApartments= sc.nextInt();
        this.nrOfIndividuals= sc.nextInt();
        this.nrOfScenes= sc.nextInt();
        this.rooms= new Room[this.nrOfNormals+this.nrOfApartments+this.nrOfIndividuals+this.nrOfScenes];
        NormalRoom.setMaxCap(sc.nextInt());
        ApartmentRoom.setMaxCap(sc.nextInt());
        IndividualSpectacleRoom.setMinCap(sc.nextInt());
        IndividualSpectacleRoom.setMaxCap(sc.nextInt());
        SceneSpectacleRoom.setMinCap(sc.nextInt());
        SceneSpectacleRoom.setMaxCap(sc.nextInt());
        int x=0;
        for(int i=x; i< x+this.nrOfNormals; ++i){
            this.rooms[i]= new NormalRoom(i+1);
            this.rooms[i].read(sc);
        }
        x+=this.nrOfNormals;
        for (int i = x; i < x+this.nrOfApartments; ++i){ 
            this.rooms[i]= new ApartmentRoom(i+1);
            this.rooms[i].read(sc);
        }
        x+=this.nrOfApartments;
        for(int i=x; i< x+this.nrOfIndividuals; ++i){
            this.rooms[i]= new IndividualSpectacleRoom(i+1);
            this.rooms[i].read(sc);
        }
        x+=this.nrOfIndividuals;
        for(int i=x; i< x+this.nrOfScenes; ++i){
            this.rooms[i]= new SceneSpectacleRoom(i+1);
            this.rooms[i].read(sc);
        }
        this.restaurant.read(sc);
        roomCounts.put(NormalRoom.class, this.nrOfNormals);
        roomCounts.put(ApartmentRoom.class, this.nrOfApartments);
        roomCounts.put(IndividualSpectacleRoom.class, this.nrOfIndividuals);
        roomCounts.put(SceneSpectacleRoom.class, this.nrOfScenes);
    }
}