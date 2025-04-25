import java.util.Scanner;

public class Menu {
    protected double cost;
    protected String name;
    public double getCost() {
        return cost;
    }
    public String getName() {
        return name;
    }
    public void read(Scanner sc){
        this.cost= sc.nextDouble();
        sc.nextLine();
        this.name= sc.nextLine();
    }
}