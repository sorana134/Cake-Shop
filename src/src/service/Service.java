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
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.stream.Collectors;

public class Service {
    protected Repository Cakerepository;
    protected Repository Orderrepository;
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
    public Cake getCake(int id) {
        return (Cake) Cakerepository.get(id);
    }
    public Map<Number, Cake> getAllCakes() {
        return Cakerepository.getAll();
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
    public Map<Number, Unique> getAllOrders() {
        return Orderrepository.getAll();}

    //using streams shows all orders that are available
    public void showAvailableOrders() {

            Map<Number, Orders> orders = (Map<Number, Orders>) Orderrepository.getAll().values().stream()
                    .filter(x -> x instanceof Orders)
                    .map(x -> (Orders) x).collect(Collectors.toMap(Orders::getId, x -> x));
           Map<Number, Orders>filtered=orders.values().stream().filter(x-> x.getIsAvailable()).collect(Collectors.toMap(Orders::getId, x -> x));
            for (Orders order : filtered.values()) {
                System.out.println(order.toString());
            }



    }
    public void showFinishedOrders() {

        Map<Number, Orders> orders = (Map<Number, Orders>) Orderrepository.getAll().values().stream()
                .filter(x -> x instanceof Orders)
                .map(x -> (Orders) x).collect(Collectors.toMap(Orders::getId, x -> x));
        Map<Number, Orders>filtered=orders.values().stream().filter(x-> x.getIsDone()).collect(Collectors.toMap(Orders::getId, x -> x));
        for (Orders order : filtered.values()) {
            System.out.println(order.toString());
        }



    }
    //show cakes of a certain size
    public void showCakesOfSize(String size) {

        Map<Number, Cake> cakes = (Map<Number, Cake>) Cakerepository.getAll().values().stream()
                .filter(x -> x instanceof Cake)
                .map(x -> (Cake) x).collect(Collectors.toMap(Cake::getId, x -> x));
        Map<Number, Cake> filtered = cakes.values().stream().filter(x -> x.getSize().equals(size)).collect(Collectors.toMap(Cake::getId, x -> x));
        for (Cake cake : filtered.values()) {
            System.out.println(cake.toString());
        }
    }
    //show cakes of a certain flavour
    public void showCakesOfFlavour(String flavour) {

        Map<Number, Cake> cakes = (Map<Number, Cake>) Cakerepository.getAll().values().stream()
                .filter(x -> x instanceof Cake)
                .map(x -> (Cake) x).collect(Collectors.toMap(Cake::getId, x -> x));
        Map<Number, Cake> filtered = cakes.values().stream().filter(x -> x.getFlavour().equals(flavour)).collect(Collectors.toMap(Cake::getId, x -> x));
        for (Cake cake : filtered.values()) {
            System.out.println(cake.toString());
        }
    }
    //show orders cheaper than a certain price
    public void showOrdersCheaperThan(double price) {

        Map<Number, Orders> orders = (Map<Number, Orders>) Orderrepository.getAll().values().stream()
                .filter(x -> x instanceof Orders)
                .map(x -> (Orders) x).collect(Collectors.toMap(Orders::getId, x -> x));
        Map<Number, Orders> filtered = orders.values().stream().filter(x -> x.getPrice() < price).collect(Collectors.toMap(Orders::getId, x -> x));
        for (Orders order : filtered.values()) {
            System.out.println(order.toString());
        }
    }



}