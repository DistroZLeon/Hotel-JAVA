import java.util.Scanner;

public class IndividualSpectacleRoom extends Room implements RoomsInterface {
    private static int maxCap, minCap;
    private static boolean isMinSet = false, isMaxSet= false;
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
    public static int getMaxCap() {
        return IndividualSpectacleRoom.maxCap;
    }
    public static void setMaxCap(int maxCap) {
        if(!IndividualSpectacleRoom.isMaxSet){
            IndividualSpectacleRoom.maxCap = maxCap;
            IndividualSpectacleRoom.isMaxSet=true;
        }
        else
        throw new IllegalStateException("maxCap has already been set for IndividualSpectacleRoom.");
    }
    public static int getMinCap() {
        return IndividualSpectacleRoom.minCap;
    }
    public static void setMinCap(int minCap) {
        if(!IndividualSpectacleRoom.isMinSet){
            IndividualSpectacleRoom.minCap = minCap;
            IndividualSpectacleRoom.isMinSet=true;
        }
        else
        throw new IllegalStateException("minCap has already been set for IndividualSpectacleRoom.");
    }
    
}