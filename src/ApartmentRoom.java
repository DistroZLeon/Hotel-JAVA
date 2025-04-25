import java.util.Scanner;

public class ApartmentRoom extends Room implements RoomsInterface {
    protected static int maxCap;
    protected boolean hasJacuzzi;
    @Override
    public double getTruePrice(){
        return this.basePrice*ApartmentRoom.maxCap*1.20;
    }
    public ApartmentRoom(){
        super(0);
    }
    public ApartmentRoom(int i){
        super(i);
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
}