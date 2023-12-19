package service;

import domain.Cake;
import domain.Orders;
import domain.Unique;
import repository.*;
import validators.CakeValidator;
import validators.OrderValidator;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

public class Service {
    protected Repository Cakerepository;
    protected static Repository Orderrepository;
    public void serviceType() throws FileNotFoundException {
        Properties properties = new Properties();
        try (FileInputStream input = new FileInputStream("settings.properties")) {
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String storageType = properties.getProperty("storage.type");


        Repository <Orders, Integer> orderRepository =null;
        Repository <Cake, Integer> cakeRepository =null;
        if ("database".equalsIgnoreCase(storageType)) {
            orderRepository = new DBOrderRepo();
            cakeRepository = new DBCakeRepo();

        } else if ("memory".equalsIgnoreCase(storageType)) {
            orderRepository = new OrderRepository();
            cakeRepository = new CakeRepository();


        } else {
            throw new IllegalArgumentException("Invalid storage type specified in settings.properties");
        }
    }
    //create constructor
    public Service(Repository Cakerepository, Repository Orderrepository) {
        this.Cakerepository = Cakerepository;
        this.Orderrepository = Orderrepository;


    }


    private OrderValidator orderValidator;
    private CakeValidator cakeValidator;


    public void CheckOrder(Orders order) {
        OrderValidator.validateOrder(order);

    }
    public void CheckCake(Cake cake) {
        CakeValidator.validateCake(cake);

    }



    //CRUD OPERATIONS FOR CAKE
    public void addCake(Cake cake) {
        Cakerepository.add(cake);
    }
    public void removeCake(int id) {
        Cakerepository.remove(id);
    }
    public void updateCake(int id, Cake cake) {
        Cakerepository.update(id, cake);
    }
    public void removeCake(Cake cake) {
       Cakerepository.remove(cake);
    }
    public Cake getCake(int id) {
        return (Cake) Cakerepository.get(id);
    }

    //CRUD OPERATIONS FOR ORDER
    public void addOrder(Orders order) {
        Orderrepository.add(order);
    }
    public void removeOrder(Object id) {
        Orderrepository.remove(id);
    }
    public void updateOrder(int id, Orders order) {
        Orderrepository.update(id, order);
    }
    public Orders getOrder(int id) {
        return (Orders) Orderrepository.get(id);
    }
    public  Map<Number, Unique> getAll() {
        return Orderrepository.getAll();}
    public Map<Number, Orders>getAllOrders()

    {
        Map <Number, Unique>all=Orderrepository.getAll();

        Map<Number, Orders> Orders = (Map<Number, Orders>) Orderrepository.getAll().values().stream()
                .filter(x -> x instanceof Orders)
                .map(x -> (Orders) x).collect(Collectors.toMap(domain.Orders::getId, x -> x));
        return Orders;
    }
    public Map<Number, Cake> getAllCakes()
    {
        Map<Number, Cake> cakes = (Map<Number, Cake>) Cakerepository.getAll().values().stream()
                .filter(x -> x instanceof Cake)
                .map(x -> (Cake) x).collect(Collectors.toMap(Cake::getId, x -> x));
        return cakes;


    }

    //using streams shows all orders that are available
    public Map<Number, Orders> showAvailableOrders(boolean disp) {

            Map<Number, Orders> orders = (Map<Number, Orders>) Orderrepository.getAll().values().stream()
                    .filter(x -> x instanceof Orders)
                    .map(x -> (Orders) x).collect(Collectors.toMap(Orders::getId, x -> x));
           Map<Number, Orders>filtered=orders.values().stream().filter(x-> x.getIsAvailable()==disp).collect(Collectors.toMap(Orders::getId, x -> x));
            for (Orders order : filtered.values()) {
                System.out.println(order.toString());
            }


        return filtered;
    }
    public Map<Number, Orders> showFinishedOrders(boolean ok) {

        Map<Number, Orders> orders = (Map<Number, Orders>) Orderrepository.getAll().values().stream()
                .filter(x -> x instanceof Orders)
                .map(x -> (Orders) x).collect(Collectors.toMap(Orders::getId, x -> x));
        Map<Number, Orders>filtered=orders.values().stream().filter(x-> x.getIsDone()==ok).collect(Collectors.toMap(Orders::getId, x -> x));
        for (Orders order : filtered.values()) {
            System.out.println(order.toString());
        }


        return filtered;
    }
    //show cakes of a certain size
    public Map<Number, Cake> showCakesOfSize(String size) {

        Map<Number, Cake> cakes = (Map<Number, Cake>) Cakerepository.getAll().values().stream()
                .filter(x -> x instanceof Cake)
                .map(x -> (Cake) x).collect(Collectors.toMap(Cake::getId, x -> x));
        Map<Number, Cake> filtered = cakes.values().stream().filter(x -> x.getSize().equalsIgnoreCase(size)).collect(Collectors.toMap(Cake::getId, x -> x));
        for (Cake cake : filtered.values()) {
            System.out.println(cake.toString());
        }
      System.out.println(filtered);
        return filtered;
    }
    //show cakes of a certain flavour
    public Map<Number, Cake> showCakesOfFlavour(String flavour) {

        Map<Number, Cake> cakes = (Map<Number, Cake>) Cakerepository.getAll().values().stream()
                .filter(x -> x instanceof Cake)
                .map(x -> (Cake) x).collect(Collectors.toMap(Cake::getId, x -> x));
        Map<Number, Cake> filtered = cakes.values().stream().filter(x -> x.getFlavour().equalsIgnoreCase(flavour)).collect(Collectors.toMap(Cake::getId, x -> x));
        for (Cake cake : filtered.values()) {
            System.out.println(cake.toString());
        }
        return filtered;
    }
    //show orders cheaper than a certain price
    public Map<Number, Orders> showOrdersCheaperThan(double price) {

        Map<Number, Orders> orders = (Map<Number, Orders>) Orderrepository.getAll().values().stream()
                .filter(x -> x instanceof Orders)
                .map(x -> (Orders) x).collect(Collectors.toMap(Orders::getId, x -> x));
        Map<Number, Orders> filtered = orders.values().stream().filter(x -> x.getPrice() < price).collect(Collectors.toMap(Orders::getId, x -> x));
       System.out.println("AAAAAAAAAAAAAa");
        System.out.println('\n');

        for (Orders order : filtered.values()) {
            System.out.println(order.toString());
        }
        return filtered;
    }



}