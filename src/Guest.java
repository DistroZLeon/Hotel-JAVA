import java.util.Scanner;

public class Guest {
    private String name, CNP;
    private boolean wantsJacuzzi, wantsAtRestaurant;
    private String[] prefferedMenus;
    private int age, nrOfPrefferedMenus, roomIndex;

    public Guest(){
    }

    public Guest(Guest other) {
        this.name = other.name;
        this.CNP = other.CNP;
        this.wantsJacuzzi = other.wantsJacuzzi;
        this.wantsAtRestaurant = other.wantsAtRestaurant;
        this.age = other.age;
        this.nrOfPrefferedMenus = other.nrOfPrefferedMenus;
        this.roomIndex = other.roomIndex;
        for(int i=0; i< this.nrOfPrefferedMenus; ++i)
            this.prefferedMenus[i]= other.prefferedMenus[i];
    }

    public String getName() {
        return name;
    }

    public String getCNP() {
        return CNP;
    }

    public boolean isWantsJacuzzi() {
        return this.wantsJacuzzi;
    }

    public boolean isWantsAtRestaurant() {
        return wantsAtRestaurant;
    }

    public void notWantsAtRestaurant(){
        this.wantsAtRestaurant=false;
        this.prefferedMenus= null;
    }

    public int getAge() {
        return this.age;
    }

    public String getPrefferedMenus(int i) {
        return prefferedMenus[i];
    }

    public int getNrOfPrefferedMenus() {
        return this.nrOfPrefferedMenus;
    }

    public int getRoomIndex(){
        return this.roomIndex;
    }

    public void setRoomIndex(int i){
        this.roomIndex=i;
    }

    public void read(Scanner sc){
        System.out.println("Enter the guest's name: ");
        this.name= sc.nextLine();
        System.out.println("\nEnter the guest's CNP: ");
        this.CNP= sc.nextLine();
        System.out.println("\nEnter the guest's age: ");
        this.age= sc.nextInt();
        System.out.println("\nFurther down there will be extra choices, if the guest wants it, they should enter 1, or 0 otherwise\nJacuzzi: ");
        int choice= sc.nextInt();
        this.wantsJacuzzi= !(choice==0);
        System.out.println("\nBreakfast at the restaurant: ");
        choice= sc.nextInt();
        if(choice==0)
            this.wantsAtRestaurant= false;
        else{
            this.wantsAtRestaurant= true;
            System.out.println("\nEnter the number of preferences for the menu:");
            this.nrOfPrefferedMenus= sc.nextInt();
            this.prefferedMenus= new String[this.nrOfPrefferedMenus];
            System.out.println("\nEnter the names of the preferences for the menu:");
            for (int i = 0; i < this.nrOfPrefferedMenus; i++) {
                this.prefferedMenus[i]= sc.nextLine();
            }
        }
    }

    @Override
    public String toString() {
        return name + " that has the CNP " + CNP + ", aged " + age + " has been placed in the Room indexed " + roomIndex+"\n";
    }
    
}