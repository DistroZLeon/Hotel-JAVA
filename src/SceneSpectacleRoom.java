import java.util.Scanner;

public class SceneSpectacleRoom extends Room implements RoomsInterface{
    private static int maxCap, minCap;
    private static boolean isMinSet = false, isMaxSet= false;
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
    public static int getMaxCap() {
        return SceneSpectacleRoom.maxCap;
    }
    public static void setMaxCap(int maxCap) {
        if(!SceneSpectacleRoom.isMaxSet){
            SceneSpectacleRoom.maxCap = maxCap;
            SceneSpectacleRoom.isMaxSet=true;
        }
        else
        throw new IllegalStateException("maxCap has already been set for SceneSpectacleRoom.");
    }
    public static int getMinCap() {
        return SceneSpectacleRoom.minCap;
    }
    public static void setMinCap(int minCap) {
        if(!SceneSpectacleRoom.isMinSet){
            SceneSpectacleRoom.minCap = minCap;
            SceneSpectacleRoom.isMinSet=true;
        }
        else
        throw new IllegalStateException("minCap has already been set for SceneSpectacleRoom.");
    }
    
}