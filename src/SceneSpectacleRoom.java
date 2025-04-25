import java.util.Scanner;

public class SceneSpectacleRoom extends Room implements RoomsInterface{
    protected static int maxCap, minCap;
    @Override
    public double getTruePrice(){
        return this.basePrice*SceneSpectacleRoom.maxCap*2;
    }
    public SceneSpectacleRoom(){
        super(0);
    }
    public SceneSpectacleRoom(int i){
        super(i);
    }
    @Override
    public void read(Scanner sc){
        this.basePrice= sc.nextDouble();
    }
}