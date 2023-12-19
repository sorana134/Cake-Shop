package gui;

import domain.Cake;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.NumberStringConverter;
import repository.DBCakeRepo;
import repository.DBOrderRepo;
import service.Service;

import java.util.Map;

public class CakeController {
    private final Service service;

    @FXML
    private TableView<Cake> cakeTableView;

    @FXML
    private Button addButton;

    @FXML
    private Button updateButton;

    @FXML
    private Button deleteButton;

    @FXML
    private ComboBox<String> sizeComboBox;
    @FXML
    private Button flavourButton;
    public CakeController(Service service) {

        this.service = service;

    }
    @FXML
    private void initialize() {
        // Call the method to populate the table for cakes
        populateTable();
    }


    @FXML

    private void handleFlavourButton() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Enter Flavour");
        dialog.setHeaderText(null);
        dialog.setContentText("Please enter the flavour:");


        dialog.showAndWait().ifPresent(this::updateTableBasedOnFlavour);
    }
    private void updateTableBasedOnFlavour(String selectedSize) {

        Map<Number, Cake> filteredCakes = service.showCakesOfFlavour(selectedSize);
        if("All".equalsIgnoreCase(selectedSize))
            populateTable();
        else {
        cakeTableView.getItems().clear();
        cakeTableView.getItems().addAll(filteredCakes.values());}
    }
    @FXML

    private void handleSizeComboBoxAction(ActionEvent event) {

        String selectedSize = sizeComboBox.getValue();

        System.out.println("Selected Size: " + selectedSize);
        if ("All".equals(selectedSize))
            populateTable();
        else
            updateTableBasedOnSize(selectedSize);
    }

    private void updateTableBasedOnSize(String selectedSize) {

        Map<Number, Cake> filteredCakes = service.showCakesOfSize(selectedSize);

        cakeTableView.getItems().clear();
        cakeTableView.getItems().addAll(filteredCakes.values());
    }

    @FXML
    private void handleAddButton() {
        // Create a new TextInputDialog
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add New Cake");
        dialog.setHeaderText(null);
        dialog.setContentText("Enter cake details (ID Size Flavour):");

        // Show the dialog and wait for the user's response
        dialog.showAndWait().ifPresent(input -> {
            // Split the input into ID, Size, and Flavour
            String[] parts = input.split(" ");
            if (parts.length == 3) {
                try {
                    // Parse the input values
                    int id = Integer.parseInt(parts[0]);
                    String size = parts[1];
                    String flavour = parts[2];

                    // Add a new empty row to the table
                    Cake newCake = new Cake(id, flavour, size);
                    cakeTableView.getItems().add(newCake);

                    // Select the last row for editing
                    cakeTableView.getSelectionModel().select(newCake);


                    cakeTableView.edit(cakeTableView.getItems().indexOf(newCake), cakeTableView.getColumns().get(0));


                    service.addCake(newCake);
                } catch (NumberFormatException e) {

                    e.printStackTrace();
                }
            }
        });
    }





    @FXML
    private void handleDeleteButton() {

        // Get the selected row and delete it
        Cake selectedCake = cakeTableView.getSelectionModel().getSelectedItem();
        if (selectedCake != null) {
            cakeTableView.getItems().remove(selectedCake);
            service.removeCake(selectedCake.getId().intValue());
        }
        populateTable();
    }


    public void populateTable() {
        cakeTableView.getItems().clear();
        cakeTableView.getColumns().clear();
        Map<Number, Cake> cakes = service.getAllCakes();
        TableColumn<Cake, Number> idColumn = new TableColumn<>("ID");
        TableColumn<Cake, String> sizeColumn = new TableColumn<>("Size");
        TableColumn<Cake, String> flavourColumn = new TableColumn<>("Flavour");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        sizeColumn.setCellValueFactory(new PropertyValueFactory<>("size"));
        flavourColumn.setCellValueFactory(new PropertyValueFactory<>("flavour"));


        cakeTableView.getColumns().addAll(idColumn, sizeColumn, flavourColumn);


        // Add cakes to the TableView
        cakeTableView.getItems().addAll(cakes.values());
      

    }



    public void handleUpdateButton(ActionEvent actionEvent) {
        // Get the selected row
        Cake selectedCake = cakeTableView.getSelectionModel().getSelectedItem();

        if (selectedCake != null) {
            // Create a TextInputDialog to get updated values from the user
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Update Cake");
            dialog.setHeaderText(null);
            dialog.setContentText("Enter updated cake details (ID Size Flavour):");

            // Show the dialog and wait for the user's response
            dialog.showAndWait().ifPresent(input -> {
                // Split the input into ID, Size, and Flavour
                String[] parts = input.split(" ");
                if (parts.length == 3) {
                    try {
                        // Parse the updated values
                        int id = Integer.parseInt(parts[0]);
                        String size = parts[1];
                        String flavour = parts[2];

                        // Update the selected cake with the new values
                        selectedCake.setId(id);
                        selectedCake.setSize(size);
                        selectedCake.setFlavour(flavour);

                        // Update the cake in the in-memory database
                        service.updateCake(id, selectedCake);

                        // Refresh the table
                        populateTable();
                    } catch (NumberFormatException e) {
                        // Handle invalid input (non-numeric ID)
                        e.printStackTrace(); // Add proper error handling
                    }
                }
            });
        }
    }

}
