import java.util.Scanner;

public class NormalRoom extends Room implements RoomsInterface{
    protected static int maxCap;
    @Override
    public double getTruePrice(){
        return this.basePrice*NormalRoom.maxCap*1.30;
    }
    public NormalRoom(){
        super(0);
    }
    public NormalRoom(int i){
        super(i);
    }
    @Override
    public void read(Scanner sc){
        this.basePrice= sc.nextDouble();
    }
}