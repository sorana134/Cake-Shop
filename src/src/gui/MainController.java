package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import service.Service;

import java.io.IOException;

public class MainController {
    private final Service service;

    public MainController(Service service) {
        this.service = service;
    }

    @FXML
    private void handleCakesButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Cakes.fxml"));
            CakeController cakeController = new CakeController(service);
            loader.setController(cakeController);
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Cakes");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleOrdersButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Orders.fxml"));
            OrderController OrdersController = new OrderController(service);
            loader.setController(OrdersController);
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("orders");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
