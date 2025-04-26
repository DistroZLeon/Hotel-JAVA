import java.util.Scanner;

public class Hotel {
    private int nrOfApartments, nrOfNormals, nrOfScenes, nrOfIndividuals;
    private Room[] rooms;
    private final Restaurant restaurant;
    public int getNrApartments() {
        return nrOfApartments;
    }
    public int getNrNormals() {
        return nrOfNormals;
    }
    public int getNrScenes() {
        return nrOfScenes;
    }
    public int getNrIndividuals() {
        return nrOfIndividuals;
    }
    public Restaurant getRestaurant() {
        return restaurant;
    }
    public Room getRoom(int i){
        return this.rooms[i];
    }
    public Hotel(){
        this.restaurant= new Restaurant();
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
            this.rooms[i] = new ApartmentRoom(i+1);
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
    }
}