import java.util.Scanner;

public class ApartmentRoom extends Room implements RoomsInterface {
    private static int maxCap;
    private static boolean isSet = false;
    private boolean hasJacuzzi;

    @Override
    public double getTruePrice(){
        double price= this.basePrice*ApartmentRoom.maxCap*1.10;
        if(this.hasJacuzzi)
            price= price* 1.30;
        return price;
    }

    public ApartmentRoom(){
        super(0);
    }

    public ApartmentRoom(int i){
        super(i);
    }

    public ApartmentRoom(ApartmentRoom other) {
        super(other);
    }

    public boolean getHasJacuzzi() {
        return this.hasJacuzzi;
    }

    public void setHasJacuzzi(boolean jacuzzi) {
        this.hasJacuzzi = jacuzzi;
    }

    @Override
    public void read(Scanner sc){
        this.basePrice= sc.nextDouble();
        int choice= sc.nextInt();
        this.hasJacuzzi= choice!=0;
    }

    public static int getMaxCap() {
        return ApartmentRoom.maxCap;
    }
    
    public static void setMaxCap(int maxCap) {
        if(!ApartmentRoom.isSet){
            ApartmentRoom.maxCap = maxCap;
            ApartmentRoom.isSet=true;
        }
        else
        throw new IllegalStateException("maxCap has already been set for ApartmentRoom.");
    }
}