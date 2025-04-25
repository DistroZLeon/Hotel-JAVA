import java.util.Scanner;

public class GuestGroup {
    protected int nrOfGuests, nrOfDays, nrOfApartments, nrOfNormals, nrOfScenes, nrOfIndividuals;
    protected int[] apartmentsArray, normalsArray, scenesArray, individualsArray;
    protected Guest[] guests;
    protected String spectacleRoomType;
    public int getNrOfGuests() {
        return nrOfGuests;
    }
    public int getNrOfDays() {
        return nrOfDays;
    }
    public Guest getGuest(int i) {
        return guests[i];
    }
    public String getSpectacleRoomType() {
        return spectacleRoomType;
    }
    public int getNrOfApartments() {
        return nrOfApartments;
    }
    public void setNrOfApartments(int nrOfApartments) {
        this.nrOfApartments = nrOfApartments;
        this.apartmentsArray= new int[nrOfApartments];
    }
    public int getNrOfNormals() {
        return nrOfNormals;
    }
    public void setNrOfNormals(int nrOfNormals) {
        this.nrOfNormals = nrOfNormals;
        this.normalsArray= new int[nrOfNormals];
    }
    public int getNrOfScenes() {
        return nrOfScenes;
    }
    public void setNrOfScenes(int nrOfScenes) {
        this.nrOfScenes = nrOfScenes;
        this.scenesArray= new int[nrOfScenes];
    }
    public int getNrOfIndividuals() {
        return nrOfIndividuals;
    }
    public void setNrOfIndividuals(int nrOfIndividuals) {
        this.nrOfIndividuals = nrOfIndividuals;
        this.individualsArray= new int[nrOfIndividuals];
    }
    public int getApartmentsArray(int i) {
        return this.apartmentsArray[i];
    }
    public void setApartmentsArray(int apartment, int i) {
        this.apartmentsArray[i] = apartment;
    }
    public int getNormalsArray(int i) {
        return this.normalsArray[i];
    }
    public void setNormalsArray(int normal, int i) {
        this.normalsArray[i] = normal;
    }
    public int getScenesArray(int i) {
        return this.scenesArray[i];
    }
    public void setScenesArray(int scene, int i) {
        this.scenesArray[i] = scene;
    }
    public int getIndividualsArray(int i) {
        return this.individualsArray[i];
    }
    public void setIndividualsArray(int individual, int i) {
        this.individualsArray[i] = individual;
    }
    public void setGuestRoomIndex(int i, int index){
        this.guests[i].setRoomIndex(index);
    }
    public void read(Scanner sc){
        System.out.println("Enter number of days for your stay: ");
        this.nrOfDays= sc.nextInt();
        System.out.println("\nEnter number of guests in your group: ");
        this.nrOfGuests= sc.nextInt();
        this.guests= new Guest[this.nrOfGuests];
        for (int i = 0; i < this.nrOfGuests; i++) {
            this.guests[i]= new Guest();
        }
        for(int i=0; i< this.nrOfGuests; ++i)
            guests[i].read(sc);
        System.out.println("\nDo you want to rent any kind of spectacle room? Enter 1 if so, or 0 otherwise: ");
        int choice= sc.nextInt();
        if(choice==1){
            System.out.println("\nWhat type of spectacle room do you want? Enter 1 for a scene spectacle room, or 0 for an individual spectacle room: ");
            choice= sc.nextInt();
            if(choice==0)
                this.spectacleRoomType= "Individual";
            else
                this.spectacleRoomType= "Scene";
        }
    }
    public void write(){

    }
}