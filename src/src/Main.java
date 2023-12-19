import gui.CakeController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import repository.* ;
import repository.DBOrderRepo;
import repository.DBRepository;
import service.Service;
import ui.MainUi;


public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        DBRepository repo = new DBCakeRepo();
        DBRepository repo1 = new DBOrderRepo();
        Service service = new Service(repo, repo1);
        CakeController controller = new CakeController(service);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/Cakes.fxml"));
        loader.setController(controller);

        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
        stage.show();
        controller.populateTable();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
