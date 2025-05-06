import java.util.Arrays;
import java.util.Scanner;

public class GuestGroup {
    private int nrOfGuests, nrOfDays, nrOfApartments, nrOfNormals, nrOfScenes, nrOfIndividuals, id, minDay;
    private int[] apartmentsArray, normalsArray, scenesArray, individualsArray;
    private Guest[] guests;
    private SpectacleRoomType spectacleRoomType;

    public int getNrOfGuests() {
        return this.nrOfGuests;
    }

    public int getNrOfDays() {
        return this.nrOfDays;
    }

    public Guest getGuest(int i) {
        return this.guests[i];
    }

    public SpectacleRoomType getSpectacleRoomType() {
        return this.spectacleRoomType;
    }

    public int getNrOfApartments() {
        return this.nrOfApartments;
    }

    public void setNrOfApartments(int nrOfApartments) {
        this.nrOfApartments = nrOfApartments;
        this.apartmentsArray= new int[nrOfApartments];
    }

    public int getNrOfNormals() {
        return this.nrOfNormals;
    }

    public void setNrOfNormals(int nrOfNormals) {
        this.nrOfNormals = nrOfNormals;
        this.normalsArray= new int[nrOfNormals];
    }

    public int getNrOfScenes() {
        return this.nrOfScenes;
    }

    public void setNrOfScenes(int nrOfScenes) {
        this.nrOfScenes = nrOfScenes;
        this.scenesArray= new int[nrOfScenes];
    }

    public int getNrOfIndividuals() {
        return this.nrOfIndividuals;
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

    public int getId(){
        return this.id;
    }

    public int getMinDay(){
        return this.minDay;
    }

    public void setMinDay(int val){
        this.minDay=val;
    }

    public GuestGroup(){
        this(0);
    }

    public GuestGroup(int index){
        this.id= index;
        this.spectacleRoomType= SpectacleRoomType.NONE;
    }

    public GuestGroup(GuestGroup other) {
        this.nrOfGuests = other.nrOfGuests;
        this.nrOfDays = other.nrOfDays;
        this.nrOfApartments = other.nrOfApartments;
        this.nrOfNormals = other.nrOfNormals;
        this.nrOfScenes = other.nrOfScenes;
        this.nrOfIndividuals = other.nrOfIndividuals;
        this.id = other.id;
        this.minDay = other.minDay;
        this.spectacleRoomType = other.spectacleRoomType;
        this.apartmentsArray = new int[other.nrOfApartments];
        this.normalsArray = new int[other.nrOfNormals];
        this.individualsArray = new int[other.nrOfIndividuals];
        this.scenesArray = new int[other.nrOfScenes];
        this.guests= new Guest[other.nrOfGuests];
        for(int i= 0; i< this.nrOfApartments; ++i)
            this.apartmentsArray[i]= other.apartmentsArray[i];
        for(int i= 0; i< this.nrOfNormals; ++i)
            this.normalsArray[i]= other.normalsArray[i];
        for(int i= 0; i< this.nrOfIndividuals; ++i)
            this.individualsArray[i]= other.individualsArray[i];
        for(int i= 0; i< this.nrOfScenes; ++i)
            this.scenesArray[i]= other.scenesArray[i];
        for(int i=0; i< this.nrOfGuests; ++i)
            this.guests[i]= other.guests[i];
    }

    public void sort(){
        Arrays.sort(this.guests, (g1, g2)-> Boolean.compare( g2.isWantsJacuzzi(), g1.isWantsJacuzzi()));
    }

    public void read(Scanner sc){
        System.out.println("Enter number of days for your stay: ");
        this.nrOfDays= sc.nextInt();
        if(this.nrOfDays<1){
            System.out.println("Invalid period of time. It will be set by default to 3.\n");
            this.nrOfDays=3;
        }
        if(this.nrOfDays>14){
            System.out.println("Invalid period of time. It will be set by default to 14.\n");
            this.nrOfDays=14;
        }
        System.out.println("\nEnter number of guests in your group: ");
        this.nrOfGuests= sc.nextInt();
        if(this.nrOfGuests<1){
            System.out.println("Invalid number of guests. It will be set by default to 1.\n");
            this.nrOfGuests= 1;
        }
        sc.nextLine();
        this.guests= new Guest[this.nrOfGuests];
        for (int i = 0; i < this.nrOfGuests; i++) {
            this.guests[i]= new Guest();
            this.guests[i].read(sc);
        }
        System.out.println("\nDo you want to rent any kind of spectacle room? Enter 1 if so, or 0 otherwise: ");
        int choice= sc.nextInt();
        if(choice==1){
            System.out.println("\nWhat type of spectacle room do you want? Enter 1 for a scene spectacle room, or 0 for an individual spectacle room: ");
            choice= sc.nextInt();
            if(choice==0)
                this.spectacleRoomType= SpectacleRoomType.INDIVIDUAL;
            else
                this.spectacleRoomType= SpectacleRoomType.SCENE;
        }
    }

    @Override
    public String toString() {
        Hotel hotel= Hotel.getInstance();
        String str= "\nFor the group with the Id " +this.id + " the minimum time slot that they have been allocated is between "+ this.minDay+ " and "+ (this.minDay+this.nrOfDays-1)+".\nThe group has reserved:\n";
        if(this.nrOfNormals!=0){
            str= str+ this.nrOfNormals+ " Normal Rooms\nThey have the Id: ";
            for(int i=0; i<this.nrOfNormals; ++i)
                str= str+ hotel.getRoom(this.normalsArray[i]).getIndex()+ " ";
            str=str+ "\n";
        }
        if(this.nrOfApartments!=0){
            str= str+ this.nrOfApartments+ " Apartment Rooms\nThey have the Id: ";
            for(int i=0; i<this.nrOfApartments; ++i)
                str= str+ hotel.getRoom(this.apartmentsArray[i]).getIndex()+ " ";
            str=str+ "\n";
        }
        str= str+ "The group has asked for a "+ spectacleRoomType.getLabel()+ " type Spectacle Room.\n";
        if(this.nrOfIndividuals!=0){
            str= str+ this.nrOfIndividuals+ " Individual Spectacle Rooms\nThey have the Id: ";
            for(int i=0; i<this.nrOfIndividuals; ++i)
                str= str+ hotel.getRoom(this.individualsArray[i]).getIndex()+ " ";
            str=str+ "\n";
        }
        if(this.nrOfScenes!=0){
            str= str+ this.nrOfScenes+ " Scene Spectacle Rooms\nThey have the Id: ";
            for(int i=0; i<this.nrOfScenes; ++i)
                str= str+ hotel.getRoom(this.scenesArray[i]).getIndex()+ " ";
            str=str+ "\n";
        }
        for(int i=0; i<this.nrOfGuests; ++i)
            str+= this.guests[i];
        str+="\n";
        return str;
    }
    
}