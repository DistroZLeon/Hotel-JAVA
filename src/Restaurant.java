import java.util.Scanner;

public class Restaurant {
    private final int[] days;
    private int nrOfSeats, nrOfMenus;
    private Menu[] menus;

    public int getNrOfGuestsThatDay(int i) {
        return this.days[i];
    }

    public void setIdDays(int val, int start, int duration) {
        for(int i= start; i< start+ duration; ++i)
            this.days[i]+= val;
    }

    public Menu getMenus(int i) {
        return this.menus[i];
    }

    public int getNrOfSeats() {
        return this.nrOfSeats;
    }

    public int getNrOfMenus() {
        return this.nrOfMenus;
    }
    
    public Restaurant(){
        this.days= new int[367];
        for(int i=1; i<366; ++i){
            this.days[i]= 0;
        }
    }
    public Restaurant(Restaurant other) {
        this.days= new int[367];
        for(int i=1; i< 366; ++i)
            this.days[i]= other.days[i];
        this.nrOfSeats = other.nrOfSeats;
        this.nrOfMenus = other.nrOfMenus;
        this.menus= new Menu[this.nrOfMenus];
        for(int i=0; i< this.nrOfMenus; ++i)
            this.menus[i]= other.menus[i];    
    }

    public void read(Scanner sc){
        this.nrOfSeats= sc.nextInt();
        this.nrOfMenus=sc.nextInt();
        this.menus= new Menu[this.nrOfMenus];
        for (int i = 0; i < this.nrOfMenus; i++) {
            this.menus[i]= new Menu();
            this.menus[i].read(sc);
        }
    }
}