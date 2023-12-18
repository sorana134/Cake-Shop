package gui;

import domain.Cake;
import javafx.fxml.FXML;
import repository.DBCakeRepo;
import repository.DBOrderRepo;
import service.Service;

import javax.swing.text.TableView;
import java.awt.*;
import java.util.Map;

public class CakeController {
    private final Service service;



    public CakeController(Service service) {
        this.service = service;
    }

    // Method to add a cake to the service
    public void addCake( Number ID, String flavour, String size) {
        Cake cake = new Cake( ID,  flavour,  size);
        service.addCake(cake);
    }

    // Method to remove a cake from the service
    public void removeCake(int id) {
        service.removeCake(id);
    }

    // Method to update a cake in the service
    public void updateCake(Number ID, String flavour, String size) {
        Cake updatedCake = new Cake(ID, flavour, size);
        service.updateCake((Integer) ID, updatedCake);
    }

    // Method to get a cake from the service
    public Cake getCake(int id) {
        return service.getCake(id);
    }

    // Method to get all cakes from the service
    public Map<Number, Cake> getAllCakes() {
        return service.getAllCakes();
    }

    // Method to show cakes of a certain size
    public void showCakesOfSize(String size) {
        service.showCakesOfSize(size);
    }

    // Method to show cakes of a certain flavour
    public void showCakesOfFlavour(String flavour) {
        service.showCakesOfFlavour(flavour);
    }

    // Method to show orders cheaper than a certain price
    public void showOrdersCheaperThan(double price) {
        service.showOrdersCheaperThan(price);
    }

    @FXML
    private TableView cakeTableView; // Assuming you have a Cake class

    @FXML
    private Button addButton;

    @FXML
    private Button updateButton;

    @FXML
    private Button deleteButton;

    // You need to set the service in the controller, either through constructor or setter
    private CakeController cakeController;

    public void CakeTableController() {
        DBOrderRepo orderRepository = new DBOrderRepo();
        DBCakeRepo cakeRepository = new DBCakeRepo();
        cakeController = new CakeController(new Service(orderRepository, cakeRepository));



    }

    // Define event handlers for the buttons

    @FXML
    private void handleAddButton() {
        // Add your logic here
        // For example, you might open a new window for adding a cake
    }

    @FXML
    private void handleUpdateButton() {
        // Add your logic here
        // For example, you might get the selected cake from the table and open a window for updating it
    }

    @FXML
    private void handleDeleteButton() {
        // Add your logic here
        // For example, you might get the selected cake from the table and delete it using your controller
    }



}
