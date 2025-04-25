import java.util.Scanner;

public class Hotel {
    protected int nrOfApartments, nrOfNormals, nrOfScenes, nrOfIndividuals;
    protected Room[] rooms;
    protected Restaurant restaurant;
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
    public void setRestaurantDay(int val, int i) {
        this.restaurant.setIdDays(val, i);
    }
    public void setRoomDay(int i,int val, int index ){
        this.rooms[i].setIdDays(val, index);
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
        NormalRoom.maxCap= sc.nextInt();
        ApartmentRoom.maxCap= sc.nextInt();
        IndividualSpectacleRoom.minCap= sc.nextInt();
        IndividualSpectacleRoom.maxCap= sc.nextInt();
        SceneSpectacleRoom.minCap= sc.nextInt();
        SceneSpectacleRoom.maxCap= sc.nextInt();
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