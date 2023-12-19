package ui;

import domain.Cake;
import domain.Orders;
import domain.Unique;
import repository.Repository;
import service.Service;
import validators.CakeValidator;
import validators.OrderValidator;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class MainUi {
    //create a service for orders and one for cakes
    public static Service ServiceType(){
        Properties properties = new Properties();
        try (FileInputStream input = new FileInputStream("settings.properties")) {
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String storageType = properties.getProperty("Repository");


        repository.Repository <Orders, Integer> orderRepository =null;
        repository.Repository <Cake, Integer> cakeRepository =null;
        if ("database".equalsIgnoreCase(storageType)) {
            orderRepository = new repository.DBOrderRepo();
            cakeRepository = new repository.DBCakeRepo();

        } else if ("memory".equalsIgnoreCase(storageType)) {
            orderRepository = new repository.OrderRepository();
            cakeRepository = new repository.CakeRepository();


        } else {
            throw new IllegalArgumentException("Invalid storage type specified in settings.properties");
        }
       return new Service(orderRepository, cakeRepository);
    }
    public static Service service=ServiceType();

    private static final Scanner scanner=new Scanner(System.in);

    public static void main(String[] args) throws ParseException, IOException {
       // examples();
        UI();
    }
    public static void UI() throws ParseException, IOException {
        boolean isRunning=true;


        Map<Number, Orders> orders;
        Map<Number, Cake> cakes;



        while(isRunning)
        {
            System.out.println("1. Add order");
            System.out.println("2. Get all orders");
            System.out.println("3. Remove order");
            System.out.println("4. Update order (including adding cakes)");
            System.out.println("5. Remove cakes from order");
            System.out.println("6. Show available orders");
            System.out.println("7. Show finished orders");
            System.out.println("8. Show cakes of a certain size");
            System.out.println("9. Show cakes of a certain flavour");
            System.out.println("10. Show orders cheaper than a given price");



            System.out.println("0. Exit");
            System.out.println("Choose an option: ");
            int option=scanner.nextInt();
            switch(option) {
                case 1:
                    add();

                    break;
                case 2:
                    getAll();

                    break;

                case 3:
                   remove();
                    break;

                case 4:
                    update();
                    break;

                case 5:
                   removeCake();
                    break;
                case 6:
                    service.showAvailableOrders(true);
                    break;
                case 7:
                    service.showFinishedOrders(true);
                    break;
                case 8:
                    System.out.println("Enter cake size: ");
                    String size = scanner.next();
                    service.showCakesOfSize(size);
                    break;
                case 9:
                    System.out.println("Enter cake flavour: ");
                    String flavour = scanner.next();
                    service.showCakesOfFlavour(flavour);
                    break;
                case 10:
                    System.out.println("Enter price: ");
                    double price = scanner.nextDouble();
                    service.showOrdersCheaperThan(price);
                    break;

                case 0:
                    isRunning = false;
                    break;

                default:
                    System.out.println("Invalid option");
            }
        }
    }
    public static void add() throws ParseException {   boolean isAvailable = true;
        System.out.println("Enter price: ");
        double price = scanner.nextDouble();
        boolean isDone = false;
        System.out.println("Enter due date of the pattern dd.MM.yyyy: ");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = scanner.next();
        try
        {

            Date dueDate = dateFormat.parse(dateStr);
        }
        catch (ParseException e)
        {
            System.out.println("Invalid date format");
            return;

        }
        Date dueDate = dateFormat.parse(dateStr);



        System.out.println("Enter number of cakes: ");
        int numberOfCakes = scanner.nextInt();
        try {

            if (numberOfCakes < 1) {
                throw new Exception("Invalid number of cakes");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }

        Map<Number, Cake> cakeList = new HashMap<>();


        for (int i = 0; i < numberOfCakes; i++) {
            System.out.println("Enter cake flavour: ");
            String cakeFlavour = scanner.next();
            System.out.println("Enter cake size: ");
            String cakeSize = scanner.next();
            Cake cake = new Cake((long) (i + 1), cakeFlavour, cakeSize);
            service.CheckCake(cake);
             service.addCake(cake);
            cakeList.put(cake.getId(), cake);
        }
        Orders orderc=new Orders(isAvailable, price, isDone, dueDate, 0,cakeList );
        service.CheckOrder(orderc);

        service.addOrder(orderc);

    }
    public static void getAll(){
        service.getAllOrders();

       //only show orders
        Map<Number, Orders> orders = service.getAllOrders();
        for (Unique order : orders.values()) {
           if (order instanceof Orders) {
                System.out.println(order.toString());
            }
        }


    }
    public static void remove(){
        System.out.println("Enter orderID: ");
        int orderID = scanner.nextInt();
        service.removeOrder(orderID);
    }
    public static void update() throws ParseException {
        // User input orderId
        System.out.println("Enter orderID: ");
        int orderID2 = scanner.nextInt();
        Orders orderToUpdate = service.getOrder(orderID2);

        // Retrieve order details
        boolean isAvailable1 = orderToUpdate.getIsAvailable();
        double price1_ = orderToUpdate.getPrice();
        boolean isDone1 = orderToUpdate.getIsDone();

        System.out.println("Enter due date of the pattern dd.MM.yyyy: ");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        String dateStr = scanner.next();
        try
        {

            Date dueDate = dateFormat.parse(dateStr);

        }
        catch (ParseException e)
        {
            System.out.println("Invalid date format");
            return;

        }
        Date dueDate = dateFormat.parse(dateStr);



        // Input the cake specifications
        System.out.println("Do you want to add cakes y/n: ");
        String answer = scanner.next();
        Map<Number, Cake> cakeMap = orderToUpdate.getCakes();


        int numberOfCakes;
        if (answer.equals("y")) {
            System.out.println("Enter number of cakes to add: ");
            numberOfCakes = scanner.nextInt();

            for (int i = 0; i < numberOfCakes; i++) {
                System.out.println("Enter cake flavour: ");
                String cakeFlavour = scanner.next();
                System.out.println("Enter cake size: ");
                String cakeSize = scanner.next();
                Cake cake = new Cake((long) (i + 1), cakeFlavour, cakeSize);
                service.CheckCake(cake);
                 service.addCake(cake);
                cakeMap.put(cake.getId(), cake);
            }
        }

        // Modify the already existing cakes in the order
        System.out.println("Modify the already existing cakes y/n: ");
        answer = scanner.next();
        if (answer.equals("y")) {
            System.out.println("Enter number of cakes to modify: ");
            numberOfCakes = scanner.nextInt();
            try {
                if (numberOfCakes > cakeMap.size()) {
                    throw new Exception("Invalid number of cakes");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return;

            }

            for (int i = 0; i < numberOfCakes; i++) {
                System.out.println("Enter cakeID for the cake to modify: ");
                Number cakeID = scanner.nextLong();
                Cake cake = service.getCake((Integer) cakeID);


                if (cake != null) {
                    System.out.println("Enter cake flavour: ");
                    String cakeFlavour = scanner.next();
                    System.out.println("Enter cake size: ");
                    String cakeSize = scanner.next();
                    cake.setFlavour(cakeFlavour);
                    cake.setSize(cakeSize);
                    Cake cake3=new Cake(cakeID,cakeFlavour, cakeSize);
                    service.CheckCake(cake3);
                    service.updateCake((Integer) cakeID, cake);
                } else {
                    System.out.println("There is no cake with this ID");
                }

            }

        }

        Orders ordertocheck=new Orders(isAvailable1, price1_, isDone1, dueDate, orderID2, cakeMap);
        service.CheckOrder(ordertocheck);
        service.updateOrder(orderID2, new Orders(isAvailable1, price1_, isDone1, dueDate, orderID2, cakeMap));
    }

    public static void removeCake(){
        //user input orderId
        System.out.println("Enter orderID: ");
        int orderID3 = scanner.nextInt();
        Orders order2 = service.getOrder(orderID3);
        //enter id for cakes to be deleted
        System.out.println("Enter cakeID: ");

        Number cakeID = scanner.nextInt();
        order2.getCakes().remove(cakeID);
    }


}



