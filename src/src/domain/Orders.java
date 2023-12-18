package domain;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class Orders implements Unique<Number>, Serializable {
    private boolean isAvailable;
    private double price;
    private boolean isDone;
    private Date dueDate;
    private Map<Number, Cake> cakes = new HashMap<>();
    private Number OrderID;
    //constructor
    public Orders(boolean isAvailable, double price, boolean isDone, Date dueDate, Integer OrderID, Map<Number, Cake> cakes){

        this.OrderID=OrderID;

        this.isAvailable=isAvailable;
        this.price=price;
        this.isDone=isDone;

        this.cakes=cakes;

        this.dueDate=dueDate;

    }

    //getters and setters
    public Map<Number, Cake> setCake(Map<Number, Cake> cakes){
        return this.cakes=cakes;
    }
    public boolean getIsAvailable(){
        return isAvailable;
    }

    public double getPrice(){
        return price;
    }
    public boolean getIsDone(){
        return isDone;
    }

    public Date getDueDate(){
        return dueDate;
    }

    public void setIsAvailable(boolean isAvailable){
        this.isAvailable=isAvailable;
    }
    public void setPrice(double price){
        this.price=price;
    }

    public void setIsDone(boolean isDone){
        this.isDone=isDone;
    }
    public void setDueDate(Date dueDate){
        this.dueDate=dueDate;
    }


    public Map<Number, Cake> getCakes() {
        return cakes;
    }
    //to string method
    @Override
    public String toString() {
        SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd.MM.yyyy");

        StringBuilder sb = new StringBuilder();
        sb.append("OrderID: ").append(OrderID).append(" Cakes:[ ");

        for (Cake cake : cakes.values()) {
            sb.append(cake.toString()).append(", ");
        }

        sb.delete(sb.length() - 2, sb.length());  // Remove the last comma and space
        sb.append("] Price: ").append(price);
        sb.append(" Due date: ").append(outputDateFormat.format(dueDate));
        sb.append(" Availability: ").append(isAvailable);
        sb.append(" Done: ").append(isDone);

        return sb.toString();
    }
    public String simpleString()
    {  Number firstCakeId = null;


        if (!cakes.isEmpty()) {
            Map.Entry<Number, Cake> firstCakeEntry = cakes.entrySet().iterator().next();
            firstCakeId = firstCakeEntry.getKey();
        }
        SimpleDateFormat outputDateFormat = new SimpleDateFormat("yyyy-dd-MM");

        assert firstCakeId != null;
        return isAvailable + "," +
                price + "," +
                isDone + "," +
                outputDateFormat.format(dueDate) + "," +
                OrderID + "," +
                Integer.parseInt(firstCakeId.toString());


    }


    //getters and setters for orderID

    @Override
    public Number getId() {
        return OrderID;
    }



    @Override
    public void setId(Number id) {
        this.OrderID=id;

    }
    //create hashmaps of cakes from string
    public static Map<Number, Cake> cakesFromString(String s){
        Map<Number, Cake> cakes = new HashMap<>();
        String[] args = s.split(";");
        for(int i = 0; i < args.length; i++){
            System.out.println("i: " + i + ", args[i]: " + args[i]);
            if(args[i].equals("Cake")){
                Cake cake = Cake.fromString(args[i+1]);
                cakes.put(cake.getId(), cake);
            }
        }
        return cakes;
    }

}
