import java.util.Scanner;

public class NormalRoom extends Room implements RoomsInterface{
    private static int maxCap;
    private static boolean isSet = false;
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
    public static int getMaxCap() {
        return NormalRoom.maxCap;
    }
    public static void setMaxCap(int maxCap) {
        if(!NormalRoom.isSet){
            NormalRoom.maxCap = maxCap;
            NormalRoom.isSet=true;
        }
        else
        throw new IllegalStateException("maxCap has already been set for NormalRoom.");
    }
    
}