import java.util.Scanner;

public class Guest {
    private String name, CNP;
    private boolean wantsJacuzzi, wantsAtRestaurant;
    private String preferredMenu;
    private int age, roomIndex;

    public Guest(){
    }
    
    public Guest(String name, String CNP, String preferredMenu, int age, int roomIndex) {
        this.name = name;
        this.CNP = CNP;
        this.preferredMenu = preferredMenu;
        this.wantsAtRestaurant= preferredMenu!= null;
        this.age = age;
        this.roomIndex = roomIndex;
    }

    public Guest(Guest other) {
        this.name = other.name;
        this.CNP = other.CNP;
        this.wantsJacuzzi = other.wantsJacuzzi;
        this.wantsAtRestaurant = other.wantsAtRestaurant;
        this.age = other.age;
        this.roomIndex = other.roomIndex;
        this.preferredMenu= other.preferredMenu;
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
        this.preferredMenu= null;
    }

    public int getAge() {
        return this.age;
    }

    public String getPreferredMenu() {
        return this.preferredMenu;
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
        boolean canContinue= false;
        while(!canContinue){
            System.out.println("\nEnter the guest's CNP, it should be a valid one: ");
            this.CNP= sc.nextLine().trim();
            if(this.CNP.length()==13&& (this.CNP.charAt(0)== '1'|| this.CNP.charAt(0)== '2'|| this.CNP.charAt(0)== '5'|| this.CNP.charAt(0)== '6'))
                canContinue= true;
        }
        canContinue= false;
        while(!canContinue){
            System.out.println("\nEnter the guest's age, it should be a valid one: ");
            this.age= sc.nextInt();
            if(this.age>=0)
                canContinue= true;
        }
        System.out.println("\nFurther down there will be extra choices, if the guest wants it, they should enter 1, or 0 otherwise\nJacuzzi: ");
        int choice= sc.nextInt();
        this.wantsJacuzzi= !(choice==0);
        System.out.println("\nBreakfast at the restaurant: ");
        choice= sc.nextInt();
        sc.nextLine();
        if(choice== 0)
            this.wantsAtRestaurant= false;
        else{
            this.wantsAtRestaurant= true;
            Hotel hotel= Hotel.getInstance();
            System.out.println("\nEnter the name of the preferenced menu from the list down below or none if you changed your mind:");
            for(int k=0; k< hotel.getRestaurant().getNrOfMenus(); ++k)
                System.out.println(hotel.getRestaurant().getMenus(k).getName());
            canContinue= false;
            while(!canContinue){
                System.out.println("Please enter the preference:");
                String preference= sc.nextLine();
                preference= preference.trim();
                if(preference.equalsIgnoreCase("None")){
                    canContinue=true;
                    this.preferredMenu= null;
                    this.wantsAtRestaurant= false;
                }
                else{
                    for(int k=0; k< hotel.getRestaurant().getNrOfMenus(); ++k){
                        if(hotel.getRestaurant().getMenus(k).getName().equalsIgnoreCase(preference)){
                            canContinue=true;
                            this.preferredMenu=preference;
                            break;
                        }
                    }
                }
            }
        }
    }

    @Override
    public String toString() {
        return name + " that has the CNP " + CNP + ", aged " + age + " has been placed in the Room indexed " + roomIndex+"\n";
    }
    
}