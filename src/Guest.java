import java.util.Scanner;

public class Guest {
    private String name, CNP;
    private boolean wantsJacuzzi, wantsAtRestaurant;
    private String preferedMenu;
    private int age, roomIndex;

    public Guest(){
    }

    public Guest(Guest other) {
        this.name = other.name;
        this.CNP = other.CNP;
        this.wantsJacuzzi = other.wantsJacuzzi;
        this.wantsAtRestaurant = other.wantsAtRestaurant;
        this.age = other.age;
        this.roomIndex = other.roomIndex;
        this.preferedMenu= other.preferedMenu;
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
        this.preferedMenu= null;
    }

    public int getAge() {
        return this.age;
    }

    public String getPreferedMenu() {
        return this.preferedMenu;
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
        sc.nextLine();
        if(choice== 0)
            this.wantsAtRestaurant= false;
        else{
            this.wantsAtRestaurant= true;
            Hotel hotel= Hotel.getInstance();
            System.out.println("\nEnter the name of the preferenced menu from the list down below or none if you changed your mind:");
            for(int k=0; k< hotel.getRestaurant().getNrOfMenus(); ++k)
                System.out.println(hotel.getRestaurant().getMenus(k).getName());
            boolean canContinue= false;
            while(!canContinue){
                System.out.println("Please enter the preference:");
                String preference= sc.nextLine();
                preference= preference.trim();
                if(preference.equalsIgnoreCase("None")){
                    canContinue=true;
                    this.preferedMenu= null;
                    this.wantsAtRestaurant= false;
                }
                else{
                    for(int k=0; k< hotel.getRestaurant().getNrOfMenus(); ++k){
                        if(hotel.getRestaurant().getMenus(k).getName().equalsIgnoreCase(preference)){
                            canContinue=true;
                            this.preferedMenu=preference;
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