import java.util.Scanner;

public class IndividualSpectacleRoom extends Room implements RoomsInterface {
    protected static int maxCap, minCap;
    @Override
    public double getTruePrice(){
        return this.basePrice*IndividualSpectacleRoom.maxCap*1.50;
    }
    public IndividualSpectacleRoom(){
        super(0);
    }
    public IndividualSpectacleRoom(int i){
        super(i);
    }
    @Override
    public void read(Scanner sc){
        this.basePrice= sc.nextDouble();
    }
}