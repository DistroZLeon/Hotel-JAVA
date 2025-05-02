import java.util.Scanner;

public abstract class Room {
    private int index;
    double basePrice;
    private int[] days;

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
        return this.index;
    }

    public int getIdDays(int i) {
        return this.days[i];
    }

    public void setIdDays(int val, int start, int duration) {
        for(int i= start; i< start+ duration; ++i)
            this.days[i]= val;
    }

    public double getBasePrice() {
        return this.basePrice;
    }
    
    public abstract void read(Scanner sc);
}