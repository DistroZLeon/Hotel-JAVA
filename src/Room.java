import java.util.Scanner;

public abstract class Room {
    protected int index;
    double basePrice;
    protected int[] days;
    public Room(){
        this(0);
    }
    public Room(int index){
        this.index=index;
        this.days= new int[367];
        for(int i=1; i<366; ++i){
            this.days[i]= 0;
        }
    }
    public int getIndex() {
        return index;
    }
    public int getIdDays(int i) {
        return days[i];
    }
    public void setIdDays(int val, int i) {
        this.days[i]= val;
    }
    public double getBasePrice() {
        return basePrice;
    }
    public abstract void read(Scanner sc);
}