package domain;
import org.sqlite.SQLiteDataSource;

import java.io.Serializable;

public class Cake implements Unique<Number>, Serializable {
    private Number ID;
    private String flavour;
    private String size;

    //constructor
    public Cake(Number ID, String flavour, String size) {

        this.ID = ID;
        this.flavour = flavour;
        //check if size is valid and throw exception if not
      //  if (!size.equals("small") && !size.equals("medium") && !size.equals("large"))
        //    throw new IllegalArgumentException("Invalid size");
        this.size = size;

    }

    public Cake() {

    }

    //getters and setters
    public String getFlavour(){
        return flavour;
    }
    public String getSize(){
        return size;
    }

    public void setID(Long ID){

        this.ID=ID;
    }

    public void setFlavour(String flavour){
        this.flavour=flavour;
    }
    public void setSize(String size){
       // if (!size.equals("small") && !size.equals("large"))
         //   throw new IllegalArgumentException("Invalid size");
        this.size=size;
    }
    //to string method
    @Override
    public String toString(){
        return "Cake ID: "+ID+" Cake flavour: "+flavour+" Cake size: "+size;
    }






    @Override
    public void setId(Number id) {
        this.ID=id;

    }

    @Override
    public Number getId() {
        return ID;
    }

    @Override
    public Object getDueDate() {
        return null;
    }
    //from string to cake object
    public static Cake fromString(String s){
        String[] args = s.split(";");
        int ID = Integer.valueOf(args[0]);
        String flavour = args[1];
        String size = args[2];
        return new Cake(ID, flavour, size);
    }
    public String SimpleString()
    {
        return ID + ","+flavour+","+size;
    }

}
