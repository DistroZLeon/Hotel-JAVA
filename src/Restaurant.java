import java.util.Scanner;

public class Restaurant {
    protected int[] days;
    protected int nrOfSeats, nrOfMenus;
    protected Menu[] menus;
    public int getDays(int i) {
        return this.days[i];
    }
    public void setIdDays(int day, int i) {
        this.days[i] = day;
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