package gui;

import domain.Cake;
import domain.Orders;
import domain.Unique;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import service.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

public class OrderController {

    @FXML
    private Button orderButton;
    @FXML
    private Button deleteButton;
    @FXML
    private TableView<Orders> orderTableView;
    @FXML
    private Button updateButton;
    @FXML
    private Button finishedButton;
    @FXML
    private Button availableButton;
    @FXML
    private Button pricingButton;

    private final Service service;

    public OrderController(Service service) {
        this.service = service;
    }

    // Initialize method called after FXML is loaded
    @FXML
    private void initialize() {
        // Call the method to populate the TableView for orders
        populateOrdersTable();
    }

    // Method to populate the TableView for orders
    public void populateOrdersTable() {
        orderTableView.getItems().clear();
        orderTableView.getColumns().clear();
        Map<Number, Orders> orders = service.getAllOrders();

        TableColumn<Orders, Number> idColumn = new TableColumn<>("ID");
        TableColumn<Orders, Date> dueDateColumn = new TableColumn<>("Due Date");
        TableColumn<Orders, Boolean> statusColumn = new TableColumn<>("Status");
        TableColumn<Orders, String> cakeColumn = new TableColumn<>("Cake");
        cakeColumn.setCellValueFactory(cellData -> {
            Map<Number, Cake> cakes = cellData.getValue().getCakes();

            // Assuming you want to display the first cake (if available) in the cell
            if (!cakes.isEmpty()) {
                Cake firstCake = cakes.values().iterator().next();
                return new SimpleObjectProperty<>(firstCake.toString());
            } else {
                return new SimpleObjectProperty<>(null); // No cake available
            }
        });

        TableColumn<Orders, Boolean> availabilityColumn = new TableColumn<>("isAvailable");
        TableColumn<Orders, Boolean> priceColumn = new TableColumn<>("price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        dueDateColumn.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("isDone"));
        cakeColumn.setCellFactory(tc -> {
            TableCell<Orders, String> cell = new TableCell<>();
            cell.setWrapText(true);
            cell.textProperty().bind(Bindings.when(cell.emptyProperty())
                    .then("")
                    .otherwise(cell.itemProperty().asString()));
            return cell;
        });
        availabilityColumn.setCellValueFactory(new PropertyValueFactory<>("isAvailable"));

        orderTableView.getColumns().addAll(idColumn, dueDateColumn, statusColumn, cakeColumn, availabilityColumn, priceColumn);

        // Add orders to the TableView
        orderTableView.getItems().addAll(orders.values());
    }

    // Define event handlers for buttons as needed
    @FXML
    private DatePicker dueDatePicker;

    @FXML
    private TextField cakeFlavourField;

    @FXML
    private TextField cakeSizeField;

    // Other necessary fields and methods...

    @FXML
    private void handleOrderButton() {
        // Create a new TextInputDialog
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add New Order");
        dialog.setHeaderText(null);
        dialog.setContentText("Enter order details (OrderID CakeID Price DueDate):");

        // Show the dialog and wait for the user's response
        dialog.showAndWait().ifPresent(input -> {
            // Split the input into CakeID, Price, and DueDate
            String[] parts = input.split(" ");
            if (parts.length == 4) {
                try {
                    // Parse the input values
                    int orderId = Integer.parseInt(parts[0]);
                    int cakeId = Integer.parseInt(parts[1]);
                    double price = Double.parseDouble(parts[2]);
                    LocalDate dueDate = LocalDate.parse(parts[3]);
                    Map<Number, Cake> cakeList = new HashMap<>();

                    Cake cake = service.getCake(cakeId);
                    if (cake == null) {

                        TextInputDialog cakeDialog = new TextInputDialog();
                        cakeDialog.setTitle("Add New Cake");
                        cakeDialog.setHeaderText(null);
                        cakeDialog.setContentText("Enter cake details (CakeFlavour, CakeSize):");

                        cakeDialog.showAndWait().ifPresent(cakeInput -> {
                            String[] cakeParts = cakeInput.split(" ");
                            if (cakeParts.length == 2) {
                                String cakeName = cakeParts[0];
                                String cakesize = cakeParts[1];
                                Cake newCake = new Cake(cakeId, cakeName, cakesize);
                                service.addCake(newCake);
                            }
                        });
                    }
                    cake=service.getCake(cakeId);
                    cakeList.put(cakeId, cake);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date parsedDate = dateFormat.parse(String.valueOf(dueDate));

                    Orders newOrder = new Orders(true, price, false, parsedDate, cakeId, cakeList);
                    newOrder.setId(orderId);
                    service.addOrder(newOrder);
                    orderTableView.getItems().add(newOrder);
                    orderTableView.getSelectionModel().select(newOrder);
                    orderTableView.edit(orderTableView.getItems().indexOf(newOrder), orderTableView.getColumns().get(0));

                    service.addOrder(newOrder);
                } catch (NumberFormatException | DateTimeParseException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleDeleteButton() {

        // Get the selected row and delete it
        Orders selectedItem = orderTableView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            orderTableView.getItems().remove(selectedItem);
            service.removeCake(selectedItem.getId().intValue());
        }
        populateOrdersTable();
    }

    @FXML
    private void uptd() {
        // Get the selected order
        Orders selectedOrder = orderTableView.getSelectionModel().getSelectedItem();

        if (selectedOrder != null) {
            // Create a TextInputDialog to get updated values from the user
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Update Order");
            dialog.setHeaderText(null);
            dialog.setContentText("Enter updated order details (ID Price Date Availability Status CakeID):");

            // Show the dialog and wait for the user's response
            dialog.showAndWait().ifPresent(input -> {
                // Split the input into ID, Price, and Date
                String[] parts = input.split(" ");
                if (parts.length == 6) {
                    try {
                        // Parse the updated values
                        int id = Integer.parseInt(parts[0]);
                        double price = Double.parseDouble(parts[1]);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        Date date = dateFormat.parse(parts[2]);
                        boolean avbl=Boolean.parseBoolean(parts[3]);
                        boolean done=Boolean.parseBoolean(parts[4]);
                        int cakeid=Integer.parseInt(parts[5]);
                        Cake cake=service.getCake(cakeid);
                        Map<Number, Cake> clist=new HashMap<>();
                        clist.put(cakeid, cake);
                        // Update the selected order with the new values
                        selectedOrder.setId(id);
                        selectedOrder.setPrice(price);
                        selectedOrder.setDueDate(date);
                        selectedOrder.setIsAvailable(avbl);

                        selectedOrder.setIsDone(done);
                        selectedOrder.setCake(clist);

                        // Update the order in the in-memory database
                        service.updateOrder(id, selectedOrder);

                        // Refresh the table
                        populateOrdersTable();
                    } catch (NumberFormatException | ParseException e) {
                        // Handle invalid input (non-numeric ID, invalid price, or date format)
                        e.printStackTrace(); // Add proper error handling
                    }
                }
            });
        }
    }

    @FXML
    private void handlefinishedButton(ActionEvent event) {

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Enter true for showing finished false for otherwise");
        dialog.setHeaderText(null);
        dialog.setContentText("Please enter the state:");


        dialog.showAndWait().ifPresent(this::updateTableState);
    }

    private void updateTableState(String avbl) {


        if(avbl.equalsIgnoreCase("all"))
        {
            populateOrdersTable();
        }
        else
        {
            Map<Number, Orders> filteredCakes = service.showFinishedOrders(Boolean.parseBoolean(avbl));
            orderTableView.getItems().clear();
            orderTableView.getItems().addAll(filteredCakes.values());
        }

    }

    @FXML
    private void handleAvailableButton(ActionEvent event) {

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Enter true for showing available false for otherwise");
        dialog.setHeaderText(null);
        dialog.setContentText("Please enter the state:");


        dialog.showAndWait().ifPresent(this::updateTableAvbl);
    }

    private void updateTableAvbl(String avbl) {


        if(avbl.equalsIgnoreCase("all"))
        {
            populateOrdersTable();
        }
        else
        {
            Map<Number, Orders> filteredCakes = service.showAvailableOrders(Boolean.parseBoolean(avbl));
            orderTableView.getItems().clear();
            orderTableView.getItems().addAll(filteredCakes.values());
        }

    }

    @FXML
    private void handlePricingButton() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Enter the price");
        dialog.setHeaderText(null);
        dialog.setContentText("Please enter the state:");


        dialog.showAndWait().ifPresent(this::upDatePrice);
    }
    private void upDatePrice(String avbl) {


        if(avbl.equalsIgnoreCase("all"))
        {
            populateOrdersTable();
        }
        else
        {
            Map<Number, Orders> filteredCakes = service.showOrdersCheaperThan(Double.parseDouble(avbl));
            orderTableView.getItems().clear();
            orderTableView.getItems().addAll(filteredCakes.values());
        }

    }


}
